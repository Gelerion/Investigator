- What can you say about the complexity of your code?
 file reading is disk based operation, in total we have to read all values
 so complexity would be:
    O(N/B)
 where B is minimal reading block size

 after loading and filtering we are running two loops, one inside another,
 but we have a flag (isSynced), so if a row was already processed it would be skipped

 inside each inner loop we are calculating symmetricDifference of two sets,
 worst case time is O(M) for new set creation and O(M^2) for symmetric difference calculation
 where M is each phrase describing terms size

 (basically all sets operations are running with amortized time of O(1), but I am taking a worst case scenario.
  Java 8 comes with little performance improvement of sets and maps, same bucket linking between elements was
  changed from List to RBTree)

 worst case file appending complexity would be O(N/B), if each pair of rows has strictly one word difference

  In summary:
  worst case: O(N^3 * M^2)
    N^3 - loop inside loop (N^2) * disk write complexity (N/B)
    M^2 - addAll to new Set(M) + symmetric difference calculation (M^2)

  amortized time: O(N^2 * M)
    N^2 - loop inside loop complexity reduced to (N) since we have isSynced flag
    M - iteration over each describing term

  it may be reduced to O(N * M), with additional space complexity,
  disk write operation should be outed form the inner loop

- How will your algorithm scale? If you had two weeks to do this task, what would you have done differently?
  What would be better?
  Vertical scaling - we may easily create multiple  instances of Investigator to handle more cores, with
  a little additional effort of new MultithreadedPhraseService creation

  Horizontal scaling - if we have many huge files and want to distribute work across the nodes
  a possible solution would be to implement custom DataRepository with RemoteIterator logic, which handles
  offsets and partitions for each running instance.
  Also we need to create some sort of coordinator services, between instances, but it is out of scope.
  Since investigator is using many small services each one could be changed to something else.



