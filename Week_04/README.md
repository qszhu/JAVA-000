Week04 作业题目（周四）：

一个简单的代码参考：[strong_end] https://github.com/kimmking/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0303/Homework03.java

1.**（选做）** 把示例代码，运行一遍，思考课上相关的问题。也可以做一些比较。

// TODO

2.**（必做）** 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？
写出你的方法，越多越好，提交到 Github。

```bash
$ javac *.java
```

a. Thread

b. wait()/notify

c. Lock/Condition

d. Semaphore

e. CountDownLatch

f. CyclicBarrier

g. ExecutorService

h. FutureTask

i: CompletableFuture

j: CompletionService

k: ForkJoinPool

l: Phaser

m: Exchanger

Week04 作业题目（周六）：

1.**（选做）** 列举常用的并发操作 API 和工具类，简单分析其使用场景和优缺点。

// TODO

2.**（选做）** 请思考：什么是并发？什么是高并发？实现高并发高可用系统需要考虑哪些因素，对于这些你是怎么理解的？

// TODO

3.**（选做）** 请思考：还有哪些跟并发类似 / 有关的场景和问题，有哪些可以借鉴的解决办法。

// TODO

4.**（必做）** 把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。
可选工具：xmind，百度脑图，wps，MindManage 或其他。

![concurrency](concurrency.png)

(image courtesy: https://time.geekbang.org/column/article/83267)

**挑战1**:

1. 10-基于基本类型和数组，实现ArrayList/LinkedList，支持自动扩容和迭代器
2. 20-基于基本类型和数组和List，HashMap/LinkedHashMap功能，处理hash冲突和扩容
3. 30-考虑List和Map的并发安全问题，基于读写锁改进安全问题
4. 30-考虑List和Map的并发安全问题，基于AQS改进安全问题
5. 30-编写测试代码比较它们与java-util/JUC集合类的性能和并发安全性

// TODO

**挑战2**:

1. 10-根据课程提供的场景，实现一个订单处理Service，模拟处理100万订单：后面提供模拟数据。
2. 20-使用多线程方法优化订单处理，对比处理性能
3. 30-使用并发工具和集合类改进订单Service，对比处理性能
4. 30-使用分布式集群+分库分表方式处理拆分订单，对比处理性能：第6模块讲解分库分表。
5. 30-使用读写分离和分布式缓存优化订单的读性能：第6、8模块讲解读写分离和缓存。

// TODO
