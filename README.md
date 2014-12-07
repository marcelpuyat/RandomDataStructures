Data Structure Implementations
-------------------
* [Order Statistics Tree](https://github.com/marcelpuyat/RandomDataStructures/blob/master/src/OrderStatsTree.java)
  * search, insert, delete, rank, select, min, max, predecessor, successor in O(height) time.
  * get in-order list, and print level-order in O(n) time.

* [Splay Tree](https://github.com/marcelpuyat/RandomDataStructures/blob/master/src/OrderedSplayTree.java)
  * subclasses off of order stats tree, so this can perform all operations of ordered stats tree in same O(height) time
  * splays nodes to root of tree on insert and search in O(height) time

* [MinMaxHeap](https://github.com/marcelpuyat/RandomDataStructures/blob/master/src/MinMaxHeap.java), or a double-ended priority queue
  * findMin and findMax in O(1) time
  * removeMin, removeMax and insert in O(log(n)) time
  * initialize from list in O(nlog(n)) time

* [MinStack](https://github.com/marcelpuyat/RandomDataStructures/blob/master/src/MinStack.java)
  * pop, push, peek, isEmpty, getMin in O(1) time

* [MinQueue](https://github.com/marcelpuyat/RandomDataStructures/blob/master/src/MinQueue.java)
  * Enqueue, isEmpty, getMin in O(1) time; peek, dequeue in O(1) amortized time
  * Leverages MinStack implementation
  
