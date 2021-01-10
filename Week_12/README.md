Week12 作业题目：

1. **（必做）** 配置 redis 的主从复制，sentinel 高可用，Cluster 集群。

* 主从 `master-slave`

```bash
$ cd master-slave
$ docker-compose up -d
$ docker-compose exec master redis-cli
127.0.0.1:6379> info
...
# Replication
role:master
connected_slaves:1
...
127.0.0.1:6379> set foo bar
OK
127.0.0.1:6379> ^C
$ docker-compose exec slave redis-cli
127.0.0.1:6379> info
...
# Replication
role:slave
master_host:redis-master
...
127.0.0.1:6379> get foo
"bar"
```

2. **（选做）** 练习示例代码里下列类中的作业题:
08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java

// TODO

3. **（选做☆）** 练习 redission 的各种功能。

// TODO

4. **（选做☆☆）** 练习 hazelcast 的各种功能。

// TODO

5. **（选做☆☆☆）** 搭建 hazelcast 3 节点集群，写入 100 万数据到一个 map，模拟和演 示高可用。

// TODO
