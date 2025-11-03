
# Домашнє завдання №1. Сортування і структури даних
Author: Viktor Syrotiuk (https://github.com/KOTAYE)<br>
Variant: V3, S7, A:B:C = 2:10:30

### Usage

To run homework:
```bash
mvn clean package
```
```bash
mvn exec:java
```
```bash
python3 plot_results.py
```

### Results
In this project, I decided to compare three different data structures — HashMap, TreeMap, and Heap — to understand how they perform in terms of speed and memory usage when the amount of data increases.
I used a benchmark that measured how many operations each structure could perform in 10 seconds and how much memory they consumed for databases of different sizes.
After running all the tests, I analyzed the results and built visual graphs to see the differences more clearly.

The first thing I noticed is that HashMap consistently showed the best performance.
It was able to handle the highest number of operations within the same time period and did not lose much speed when the dataset became large.
This is because HashMap works with constant-time operations on average (O(1)), which makes it very efficient for lookups, insertions, and deletions.
Even when the dataset reached 100,000 students, HashMap stayed very fast and stable.

TreeMap, on the other hand, was slower, but still showed good and predictable performance.
Its time complexity is O(log n) because it uses a balanced binary search tree internally, which means each operation takes a bit longer, especially as the dataset grows.
However, TreeMap keeps the elements sorted, which can be very useful in situations where order matters — for example, when we need to iterate through keys in a specific sequence.

The third structure, Heap, performed the worst in my experiments.
It handled far fewer operations per second, and its performance dropped dramatically as the data size increased.
This result shows that Heap is not suitable for general database-like operations such as searching or updating elements.
It’s better for more specific tasks, like managing a priority queue where we only need to extract the smallest or largest element efficiently.

When I analyzed memory usage, the results were also quite different.
HashMap again showed the most efficient behavior — it used the least amount of memory compared to the other structures.
TreeMap and Heap required more memory, sometimes up to 40% more for the same number of records.
For example, when the dataset size reached 100,000 students, TreeMap used over 100 MB of memory, while HashMap used around 69 MB and Heap around 61 MB.
This difference becomes important when working with large datasets, where memory optimization really matters.

Overall, based on my experiment, I can conclude that HashMap is the best general-purpose structure for this type of task.
It provides both high speed and reasonable memory usage.
TreeMap is a good choice when the order of elements is important, but you have to be ready to sacrifice some performance and memory.
Heap is only useful in more specific algorithms, for example when implementing a scheduler or a priority-based system.

Doing this benchmark helped me understand much better how different data structures behave not only in theory but also in real practice.
It showed me that small differences in complexity can have a big impact when the dataset becomes large.
I also learned how to measure performance and memory consumption, visualize the results with Python, and interpret them properly.
Overall, this project was a valuable experience for me as a computer science student.
