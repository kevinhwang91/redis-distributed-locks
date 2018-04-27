# redis-distributed-locks

## 总览
- 整合了mybatis+redis+spring boot
- 提供了详细的测试
- redis分布式锁的实现

## 快速开始
1. 安装并配置mysql
2. 在sql导入test.sql
3. 安装并配置redis
4. 设置好application.yml的相关配置
5. 按序启动MybatisTests、RedisTests和RedisLockTests，检查配置是否有误

## 不足
- 分布式锁暂不支持可重入性，如果想要完整分布式锁可以引用redisson框架



