Week12 作业题目：

1. **（必做）** 配置 redis 的主从复制，sentinel 高可用，Cluster 集群。

* 主从：见 `master-slave/`

```bash
$ cd master-slave
$ docker-compose up -d

# 主节点信息
$ docker-compose exec master redis-cli
127.0.0.1:6379> info replication
...
role:master
connected_slaves:1
...

# 在主节点插入记录
127.0.0.1:6379> set foo bar
OK

# 从节点信息
$ docker-compose exec slave redis-cli
127.0.0.1:6379> info replication
...
role:slave
master_host:redis-master
...

# 在从节点读取记录
127.0.0.1:6379> get foo
"bar"
```

* sentinel：见 `sentinel/`

```bash
$ cd sentinel
$ docker-compose up -d

# 主节点信息
$ docker-compose exec master redis-cli
127.0.0.1:6379> info replication
...
role:master
connected_slaves:2
...

# 从节点信息
$ docker-compose exec slave-1 redis-cli
127.0.0.1:6379> info replication
...
role:slave
master_host:172.23.0.2
master_port:6379
master_link_status:up
...

# 哨兵节点信息
$ docker-compose exec sentinel-1 redis-cli -p 26379
127.0.0.1:26379> info sentinel
...
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=172.23.0.2:6379,slaves=2,sentinels=3

# 模拟主节点下线
$ docker-compose stop master

# 从节点自动提升为主节点
$ docker-compose exec slave-1 redis-cli
127.0.0.1:6379> info replication
...
role:master
connected_slaves:1
...

# 原主节点重新上线
$ docker-compose start master

# 原主节点现为从节点
$ docker-compose exec master redis-cli
127.0.0.1:6379> info replication
...
role:slave
master_host:172.23.0.4
master_port:6379
master_link_status:up
...

# 现主节点信息，集群节点数量已恢复
$ docker-compose exec slave-1 redis-cli            
127.0.0.1:6379> info replication
...
role:master
connected_slaves:2
...
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
