# Evisit Coding Challenge

# The Challenge

Imagine your team has developed a web service that receives requests from about 20 million unique IP addresses every day. You want to keep track of the IP addresses that are making the most requests to your service each day. Your job is to write a program that (1) tracks these IP addresses in memory (don’t use a database), and (2) returns the 100 most common IP addresses.

# What would you do differently if you had more time?

I would implement my own sorting algorithm and map data structure. I would return a list of map entries in the getTop100() function. I would try to avoid a full-blown sorting alogirthm to decrease complexity.

# What is the runtime complexity of each function?
- requestHandled(): O(n*Log n)
- getTop100(): O(1)
- clear(): O(n)

# How does your code work?

- My solution is written in Java 11 and utilizes standard Java libraries.
- I store the IP addresses and their counts in a hashmap, incrementing the counts as necessary.
- When requestHandled(String address) is called, I either add the address to the hashmap or increment its count if it's already stored.
- In the requestHandled() I start a new thread that manipulates a synchronized list of 100 most popular IP addresses. It populates this list and sorts it accordingly, as well as mutates the list in case an address is higher than the current lowest-count address in the list.
- I return a list of Top 100 IP addresses, with the most common first, in getTop100().
- I delete the data from both the hashmap and the list with the clear() method. 


# What other approaches did you decide not to pursue?

I played around with a queue as well as a linked list and a tree map, but a hashmap proved to be the best data structure. 

# How would you test this?

I would try implementing this in a Spring Boot application and then write a client in Python using requests library to throw large amounts of data at the backend.
