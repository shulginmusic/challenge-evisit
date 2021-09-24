package challenge;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        //New POJO of type Challenge with the ipAddressesMap passed in the constructor
        Challenge c = new Challenge();

        //Handle the same IP 3 times
        for (int i = 0; i < 3; i ++) {
            c.requestHandled("Fairly Popular IP Address");
        }

        //Handle another IP 4 times
        for (int i = 0; i < 4; i++) {
            c.requestHandled("The Most Popular IP Address");
        }

        //Handle 1000 random UUIDs (in the real world this would be IP addresses)
        for (int i = 0; i < 1000; i ++) {
            long startTime = System.nanoTime();
            c.requestHandled(UUID.randomUUID().toString());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("Duration: " + duration + " ms");
//            System.out.println("Iteration # " + i);
        }

        //Get top 100, with the most popular IP addresses at the top
        System.out.println(c.getTop100().toString());

        c.clear();
    }
}

class Challenge {
    //This hashmap will store all of the ipAddresses and the number of times they were used to call requestHandled()
    //This is a key-value data structure, where the key is the IP address as a String, and the count is an Integer
    HashMap<String, Integer> ipAddresses = new HashMap();

    //A list of 100 most common IP addresses
    List<String> top100List = Collections.synchronizedList(new ArrayList<>());

    /**
     * requestHandled(String address)
     * This function accepts a string containing an IP address like "145.87.2.109".
     * This function will be called by the web service every time it handles a request.
     * The calling code is outside the scope of this project.
     * Since it is being called very often, this function needs to have a fast runtime.
     *
     * @param address the IP address of the request
     */

    //Complexity: O(1 + n*Log n)
    public void requestHandled(String address) {
        //Declare an Optional for the value associated with the address provided if it exists in ipAddresses already,
        // or else store an empty optional
        Optional<Integer> countOptional = Optional.ofNullable(ipAddresses.get(address));

        //If optional is not empty
        if (countOptional.isPresent()) {
            //increment the count associated with the address by 1
            ipAddresses.replace(address, countOptional.get(), countOptional.get() + 1);
        } else {
            //If countOptional is empty, put a new key-value pair in ipAddresses
            ipAddresses.put(address, 1);
        }

        //We declare this handler after count was incremented
        //This handler is happening in a different thread of the application
        //To lighten up the load of the main thread
        //Declare a threaded handler of top 100
        Top100Handler top100Handler = new Top100Handler(this, address);

        System.out.println("Request handled for IP address: " + address);
    }

    public List<String> getTop100() {
        return top100List;
    }

    //Clear method
    public void clear() {
        ipAddresses.clear();
        top100List.clear();
    }
}

class Top100Handler implements Runnable {

    Challenge c;
    String address;
    Thread thread;

    public Top100Handler(Challenge c, String address) {
        this.c = c;
        this.address = address;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        checkTop100();
    }

    /**
     * checkTopOneHundred()
     * Helper method to keep track and update 100 most common IP addresses
     */

    //Complexity: O(n*Log n)
    private synchronized void checkTop100() {

        //If top 100 is empty
        if (c.top100List.isEmpty()) {
            //Just add the address to top100
            c.top100List.add(address);
            return;
        }

        //If top 100 is less than 100 in size
        if (c.top100List.size() < 100) {
            //And doesn't yet contain this address
            if (!c.top100List.contains(address)) {
                //Add to top 100
                c.top100List.add(address);
            }
            //Sort the list
            sortTop100();
            return;
        }

        //If top 100 has 100 addresses...

        //If address is already in top 100, just sort the list since the address count was incremented by one
        if (c.top100List.contains(address)) {
            sortTop100();
        } else {
            //if address is not in top 100
            //get the last address and its count
            String currentLastAddress = c.top100List.get(99);
            int currentLowestCount = c.ipAddresses.get(currentLastAddress);

            //Compare against the address's count
            int addressCount = c.ipAddresses.get(address);
            if (addressCount > currentLowestCount) {
                //If the address's count is larger than the current last one's, replace
                //In case of a tie the old address stays (but only if it's a tie with the lowest count)
                c.top100List.remove(currentLastAddress);
                c.top100List.add(address);
                //Sort again
                sortTop100();
            }
        }
    }

    /**
     * Helper method to sort top100 in a descending order by the corresponding count (value)
     */
    private void sortTop100() {
        //Sort the list by comparing the address's counts
        c.top100List.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(c.ipAddresses.get(o2), c.ipAddresses.get(o1));
            }
        });
    }


}