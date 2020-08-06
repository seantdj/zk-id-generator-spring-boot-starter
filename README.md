# 基于zookeeper的分布式id生成器

总体思想其实是参考了mybatis的接口代理模式，也利用了Spring对增加了注释`@ZkIdWorker`的类进行管理

核心代码参见：
扫描成为spring管理的bean
`ClassPathWokerScanner`
`ZkIdWorkerBeanDefinitionRegistrar`

动态代理
`IdWorkProxy`

## 如何使用

在SpringBoot 启动类上添加
`@EnableZkIdWorker`

在配置文件上添加zookeeper的连接地址
```yaml
zk-id-worker:
   conn: 10.10.0.76:2181
```
创建一个id生成器的接口
```java
@ZkIdWorker
public interface IdWorkerTest {

    @ZkIdInvoker("/order")
    Long getOrderId();

    @ZkIdInvoker("/machine")
    Long getMachineId();

}
```
`@ZkIdWorker`注解说明这是一个zookeeper的id生成器

`@ZkIdInvoker("/order")` 说明这个方法会被代理，在zk中创建临时顺序节点的路径前缀是`/order`

使用的时候和调用mybatis的dao一样即可。

目前有个问题
因为zk的临时顺序节点，不管什么前缀开头，id都是顺序递增的，例如/order0000000001 /machine0000000002
所以在接口中定义多个方法的意义不大，而且这个id也不连续，只是顺序递增
性能问题还没考虑过，如果要求性能比较高也可以通过一次性生成一批进行缓存
