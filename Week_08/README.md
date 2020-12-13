Week08 作业题目（周四）：

1.**（选做）** 分析前面作业设计的表，是否可以做垂直拆分。

// TODO

2.**（必做）** 设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

// TODO

3.**（选做）** 模拟 1000 万的订单单表数据，迁移到上面作业 2 的分库分表中。

// TODO

4.**（选做）** 重新搭建一套 4 个库各 64 个表的分库分表，将作业 2 中的数据迁移到新分库。

// TODO

Week08 作业题目（周六）：

1.**（选做）** 列举常见的分布式事务，简单分析其使用场景和优缺点。

* XA
* TCC
* SAGA
* AT

// TODO

2.**（必做）** 基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。

// TODO

3.**（选做）** 基于 ShardingSphere narayana XA 实现一个简单的分布式事务 demo。

// TODO

4.**（选做）** 基于 seata 框架实现 TCC 或 AT 模式的分布式事务 demo。

// TODO

5.**（选做☆）** 设计实现一个简单的 XA 分布式事务框架 demo，只需要能管理和调用 2 个 MySQL 的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。

// TODO

6.**（选做☆）** 设计实现一个 TCC 分布式事务框架的简单 Demo，需要实现事务管理器，不需要实现全局事务的持久化和恢复、高可用等。

见`Transaction`

* `ItemService`和`UserService`模拟已有的减库存和扣款服务
* `shop_item.sql`和`shop_user.sql`分别为对应的数据库
* `ReduceStockOperation`和`WithdrawBalanceOperation`将对应的服务包装为TCC操作
* `TCCTransactionManager`实现最简的TCC事务管理
* 分别模拟了事务成功，扣款失败和减库存失败的情况，输出如下：

```
Success transaction
balance before: 100000000
stock before: 200
try:
Freeze user 1 balance with amount 100
Reserve item 1 stock with quantity 1
confirm:
Withdraw user 1 balance with amount 100
Reduce item 1 stock with quantity 1
balance after: 99999900
stock after: 199

Failed 1st operation
balance before: 99999900
stock before: 199
try:
fail
cancel:
Unfreeze user 1 balance with amount 100
Unreserve item 1 stock with quantity 1
balance after: 99999900
stock after: 199

Failed 2nd operation
balance before: 99999900
stock before: 199
try:
Freeze user 1 balance with amount 100
fail
cancel:
Unfreeze user 1 balance with amount 100
Unreserve item 1 stock with quantity 1
balance after: 99999900
stock after: 199
```

// TODO
* AOP
* RPC框架集成

7.**（选做☆）** 设计实现一个 AT 分布式事务框架的简单 Demo，仅需要支持根据主键 id 进行的单个删改操作的 SQL 或插入操作的事务。

// TODO
