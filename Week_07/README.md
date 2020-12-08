Week07 作业题目（周四）：

1.**（选做）** 用今天课上学习的知识，分析自己系统的 SQL 和表结构

// TODO

2.**（必做）** 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

见`InsertOrders/`

测试方法：

* `InsertOrderSingleStatements`: 使用`PreparedStatement#execute()`一条一条插入 
* `InsertOrderBatchStatements`: 使用`PreparedStatement#executeBatch()`批量插入 
* `InsertOrderBatchNoAutoCommit`: 在上述基础上关闭auto commit
* `InsertOrderRewriteBatchedStatements`: 使用`RewriteBatchedStatements`合并插入语句
* `InsertOrderPooling`: 使用HikariCP连接池，在线程池中执行批量插入

测试结果：

测试方法/数据量 | 100K | 1M
--- | --- | --- 
`InsertOrderSingleStatements` | 30586ms | N/A
`InsertOrderBatchStatements` | 24226ms | N/A
`InsertOrderBatchNoAutoCommit` | 10094ms | N/A
`InsertOrderRewriteBatchedStatements` | 4199ms | 103326ms
`InsertOrderPooling` | 1763ms | 47116ms

// TODO
* 使用自增ID顺序插入
* 插入前去掉索引

3.**（选做）** 按自己设计的表结构，插入 1000 万订单模拟数据，测试不同方式的插入效率

// TODO

4.**（选做）** 使用不同的索引或组合，测试不同方式查询效率

// TODO

5.**（选做）** 调整测试数据，使得数据尽量均匀，模拟 1 年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）

// TODO

6.**（选做）** 尝试自己做一个 ID 生成器（可以模拟 Seq 或 Snowflake）

// TODO

7.**（选做）** 尝试实现或改造一个非精确分页的程序

// TODO

Week07 作业题目（周六）：

1.**（选做）** 配置一遍异步复制，半同步复制、组复制

### Docker国内镜像

* 编辑`~/.docker/daemon.json`
```json
{
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com"
  ]
}
```
* 重启docker

### 启动mysql实例

```bash
$ docker-compose up
```

### 记录master ip
```bash
$ docker-compose exec mysql_master cat /etc/hosts
127.0.0.1       localhost
::1     localhost ip6-localhost ip6-loopback
fe00::0 ip6-localnet
ff00::0 ip6-mcastprefix
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
172.19.0.2      360da63ec411
```

### 记录日志文件名和同步位置
```bash
$ mysql -h 127.0.0.1 -P 4406 -u root -p -e "show master status"
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000003 |      154 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
```

### 配置主从复制

```bash
$ mysql -h 127.0.0.1 -P 4407 -u root -p

mysql> change master to MASTER_HOST='172.19.0.2',MASTER_USER='root',MASTER_PASSWORD='example',MASTER_LOG_FILE='mysql-bin.000003',MASTER_LOG_POS=154;
mysql> start slave;
mysql> show slave status\G
```

// TODO

2.**（必做）** 读写分离 - 动态切换数据源版本 1.0

见`Datasource1`。使用`AbstractRoutingDataSource`，切换前需手动设置一个ThreadLocal变量

// TODO
* 使用自定义注解标注需使用哪种数据源
* 根据要执行的SQL自动选择数据源
* 使用AOP

3.**（必做）** 读写分离 - 数据库框架版本 2.0

// TODO

4.**（选做）** 读写分离 - 数据库中间件版本 3.0

// TODO

5.**（选做）** 配置 MHA，模拟 master 宕机

// TODO

6.**（选做）** 配置 MGR，模拟 master 宕机

// TODO

7.**（选做）** 配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构

// TODO

### 参考资料
* https://medium.com/@sagar.dash290/docker-container-using-master-slave-approach-by-mysql-5-7-d5cbe25c115b