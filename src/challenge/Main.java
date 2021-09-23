package challenge;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        Challenge c = new Challenge(map);

        c.requestHandled("Fairly Popular IP Address");
        c.requestHandled("Fairly Popular IP Address");
        c.requestHandled("Fairly Popular IP Address");

        c.requestHandled("The Most Popular IP Address");
        c.requestHandled("The Most Popular IP Address");
        c.requestHandled("The Most Popular IP Address");
        c.requestHandled("The Most Popular IP Address");

        for (int i = 0; i < 200; i ++) {
            long startTime = System.nanoTime();
            c.requestHandled(UUID.randomUUID().toString());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("Duration: " + duration + " ms");
        }

        c.requestHandled("The Most Popular IP Address");
        c.requestHandled("The Most Popular IP Address");

        System.out.println(c.getTop100().toString());

    }
}

class Challenge {
    //This hashmap will store all of the ipAddresses and the number of times they were used to call requestHandled()
    //This is a key-value data structure, where the key is the IP address as a String, and the count is an Integer
    HashMap<String, Integer> ipAddresses = new HashMap<>();

//    //Top 100 TODO: describe
    ArrayList<String> top100 = new ArrayList<>(100);

    public Challenge(HashMap<String, Integer> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    /**
     * requestHandled(String address)
     * This function accepts a string containing an IP address like “145.87.2.109”.
     * This function will be called by the web service every time it handles a request.
     * The calling code is outside the scope of this project.
     * Since it is being called very often, this function needs to have a fast runtime.
     *
     * @param address the IP address of the request
     */

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
            ipAddresses.put(address, 0);
        }
        //Call checkTop100 to keep track of 100 most common IP addresses
        checkTop100(address);
        System.out.println("Request handled for IP address: " + address);
    }

    //call checkTopOneHundred()

    /**
     * top100()
     * This function should return the top 100 IP addresses by request count, with the highest traffic IP address first.
     * This function also needs to be fast. Imagine it needs to provide a quick response (< 300ms)
     * to display on a dashboard, even with 20 millions IP addresses. This is a very important requirement.
     * Don’t forget to satisfy this requirement.
     */
    public List<String> getTop100() {
        return top100;
    }

    /**
     * clear()
     * Called at the start of each day to forget about all IP addresses and tallies.
     */
    public void clear() {
        ipAddresses.clear();
        top100.clear();
    }

    /**
     * checkTopOneHundred()
     * Helper method to keep track and update 100 most common IP addresses
     */
    private void checkTop100(String address) {
        String currentLastAddress;
        int currentLowestCount;

        //If empty
        if (top100.isEmpty()) {
            top100.add(address);
            return;
        }

        //If less than 100
        if (top100.size() < 100) {
            if (!top100.contains(address)) {
                top100.add(address);
            }
            sortTop100();
            return;
        }

        //If top 100 has 100 addresses...
//        //Sort the list by comparing the counts
//        sortTop100();
//
        //If address is already in top 100, just sort the list since its count was incremented by one
        if (top100.contains(address)) {
            sortTop100();
        } else {
            //if address is not in top 100
            //get the last address and its count
            currentLastAddress = top100.get(99);
            currentLowestCount = ipAddresses.get(currentLastAddress);

            //Compare against the address's count
            int addressCount = ipAddresses.get(address);
            if (addressCount > currentLowestCount) {
                //If the address's count is larger than the current last one's, replace
                //In case of a tie the old address stays (but only if it's a tie with the lowest count)
                top100.remove(currentLastAddress);
                top100.add(address);
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
        top100.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(ipAddresses.get(o1), ipAddresses.get(o2));
            }
        });
        //Reverse the list so it's in descending order
        Collections.reverse(top100);
    }

}



















