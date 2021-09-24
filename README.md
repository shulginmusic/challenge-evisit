# Evisit Coding Challenge

# What would you do differently if you had more time?

I would implement my own sorting algorithm and map data structure, as well as returning a sorted map or a list of map entries in the getTop100() function

# What is the runtime complexity of each function?
- requestHandled(): O(1 + n*Log n)
- getTop100(): O(1)
- clear(): O(n)

# How does your code work?

I store the IP addresses and their counta in a hashmap, incrementing the counts as necessary. I then start a new thread that manipulates a synchronized list of 100 
most popular IP addresses. It populates this list and sorts it accordingly, as well as mutates the list in case an address is higher than the current lowest-count address in the list. I also delete the data from both the hashmap and the list with the clear() method. 


# What other approaches did you decide not to pursue?

# I played around with a queue as well as a linked list, but a hashmap proved to be the best data structure. 


# How would you test this?

I would try implementing this in a Spring Boot application and then write a client in Python using requests library to throw large amounts of data at the backend.
