Week11 作业题目：

1. **（选做）** 命令行下练习操作 Redis 的各种基本数据结构和命令。

// TODO

2. **（选做）** 分别基于 jedis，RedisTemplate，Lettuce，Redission 实现 redis 基本操作的 demo，可以使用 spring-boot 集成上述工具。

// TODO

3. **（选做）** spring 集成练习:

    实现 update 方法，配合 @CachePut

    实现 delete 方法，配合 @CacheEvict

    将示例中的 spring 集成 Lettuce 改成 jedis 或 redisson

// TODO

4. **（必做）** 基于 Redis 封装分布式数据操作：

* 在 Java 中实现一个简单的分布式锁；

见 `redis/**/DLock.java`

其中解锁通过lua脚本实现。10线程各减1万次计数。不加锁的情况下输出如下：

```
total count 100000
after 80670
```

加锁的情况下输出如下：

```
total count 100000
after 0
```

* 在 Java 中实现一个分布式计数器，模拟减库存。

见 `redis/**/DCounter.java`

通过Redis事务实现。10线程减10万库存，输出如下：

```
before: 100000
Thread 22 completed 9606 failed 1471
Thread 24 completed 8886 failed 1487
Thread 21 completed 10813 failed 1455
Thread 26 completed 10926 failed 1449
Thread 25 completed 9577 failed 1479
Thread 29 completed 10010 failed 1467
Thread 30 completed 10081 failed 1467
Thread 27 completed 9626 failed 1473
Thread 28 completed 9875 failed 1466
Thread 23 completed 10600 failed 1459
elasped 17833 ms
after: 0
```

通过lua脚本实现，输出如下：

```
before: 100000
Thread 23 completed 9992 failed 0
Thread 27 completed 10006 failed 0
Thread 22 completed 9991 failed 0
Thread 25 completed 9992 failed 1
Thread 28 completed 10013 failed 1
Thread 21 completed 9993 failed 1
Thread 30 completed 10018 failed 1
Thread 29 completed 10003 failed 1
Thread 24 completed 10002 failed 1
Thread 26 completed 9989 failed 1
elasped 2280 ms
after: 0
```

5. **（选做）** 基于 Redis 的 PubSub 实现订单异步处理

// TODO

1. **（挑战☆）** 基于其他各类场景，设计并在示例代码中实现简单 demo：

    实现分数排名或者排行榜；

    实现全局 ID 生成；
    
    基于 Bitmap 实现 id 去重；
    
    基于 HLL 实现点击量计数；
    
    以 redis 作为数据库，模拟使用 lua 脚本实现前面课程的外汇交易事务。

// TODO

2. **（挑战☆☆）** 升级改造项目：

    实现 guava cache 的 spring cache 适配；

    替换 jackson 序列化为 fastjson 或者 fst，kryo；
    
    对项目进行分析和性能调优。

// TODO

3. **（挑战☆☆☆）** 以 redis 作为基础实现上个模块的自定义 rpc 的注册中心。

// TODO
