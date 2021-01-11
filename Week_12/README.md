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

* cluster: 见 `cluster`

```bash
$ cd cluster
$ docker-compose up
...
redis-1_1        | 1:M 11 Jan 2021 07:55:08.467 # IP address for this node updated to 172.42.0.2
redis-3_1        | 1:M 11 Jan 2021 07:55:08.531 # IP address for this node updated to 172.42.0.4
redis-2_1        | 1:M 11 Jan 2021 07:55:08.631 # IP address for this node updated to 172.42.0.3
redis-cluster_1  | >>> Performing hash slots allocation on 3 nodes...
redis-cluster_1  | Master[0] -> Slots 0 - 5460
redis-cluster_1  | Master[1] -> Slots 5461 - 10922
redis-cluster_1  | Master[2] -> Slots 10923 - 16383
redis-cluster_1  | M: e0a27dfa2b7fa993e6fe20d4494119474a657511 172.42.0.2:6379
redis-cluster_1  |    slots:[0-5460] (5461 slots) master
redis-cluster_1  | M: 876b8bb630cf49a65621074e4e2aa009df083b13 172.42.0.3:6379
redis-cluster_1  |    slots:[5461-10922] (5462 slots) master
redis-cluster_1  | M: 1f056958348a1ed7842a404efab734e6255d583f 172.42.0.4:6379
redis-cluster_1  |    slots:[10923-16383] (5461 slots) master
redis-cluster_1  | >>> Nodes configuration updated
redis-cluster_1  | >>> Assign a different config epoch to each node
redis-cluster_1  | >>> Sending CLUSTER MEET messages to join the cluster
redis-cluster_1  | Waiting for the cluster to join
redis-1_1        | 1:M 11 Jan 2021 07:55:09.736 # Cluster state changed: ok
redis-2_1        | 1:M 11 Jan 2021 07:55:09.938 # Cluster state changed: ok
redis-3_1        | 1:M 11 Jan 2021 07:55:09.973 # Cluster state changed: ok
redis-cluster_1  | .
redis-cluster_1  | >>> Performing Cluster Check (using node 172.42.0.2:6379)
redis-cluster_1  | M: e0a27dfa2b7fa993e6fe20d4494119474a657511 172.42.0.2:6379
redis-cluster_1  |    slots:[0-5460] (5461 slots) master
redis-cluster_1  | M: 876b8bb630cf49a65621074e4e2aa009df083b13 172.42.0.3:6379
redis-cluster_1  |    slots:[5461-10922] (5462 slots) master
redis-cluster_1  | M: 1f056958348a1ed7842a404efab734e6255d583f 172.42.0.4:6379
redis-cluster_1  |    slots:[10923-16383] (5461 slots) master
redis-cluster_1  | [OK] All nodes agree about slots configuration.
redis-cluster_1  | >>> Check for open slots...
redis-cluster_1  | >>> Check slots coverage...
redis-cluster_1  | [OK] All 16384 slots covered.
...
```
```bash
$ docker-compose exec redis-1 redis-cli
127.0.0.1:6379> set foo bar
-> Redirected to slot [12182] located at 172.42.0.4:6379
OK
172.42.0.4:6379> set hello world
-> Redirected to slot [866] located at 172.42.0.2:6379
OK
172.42.0.2:6379> get foo
-> Redirected to slot [12182] located at 172.42.0.4:6379
"bar"
172.42.0.4:6379> get hello
-> Redirected to slot [866] located at 172.42.0.2:6379
"world"
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
