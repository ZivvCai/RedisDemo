# RedisDemo
SpringBoot+Redis+RabbitMQ 实现秒杀系统的小demo

这个Demo基于SpringBoot，集成了Redis做分布式锁和RabbitMQ做消息队列模拟订单的秒杀和下单操作。

##### 项目启动：

1、项目源码下载下来后，直接启动RedisDemoApplication即可，相关配置可在application.properties配置文件中可以进行修改；

2、项目集成了Redis和RabbitMQ，如果本地需要启动的话需要预先安装下，网上一搜都有windows安装的，方便；

3、使用的Mysql作为数据库，t_good表和t_order表存储商品信息和订单信息，表结构字段可在代码里面domain的实体类中查看，一一对应的；

4、由于使用的Lombok，用IDEA的话需要装一下Lombok的插件；

5、127.0.0.1:9090/seckill?goodId=1这种url即可进行测试，但是一般秒杀系统都需要多并发进行测试，我这里用的是ab压测，来模拟多并发的情况，需要的话也可以装一个。

#### 关于Demo

Redis做分布式锁直接使用的是Redisson框架，自定义一个注解@RedissonLock，然后在请求接口处加上注解，通过AOP进行拦截，在拦截器中进行分布式锁的获取和判断。通过redis的分布式锁来保证商品不会超卖等情况。

RabbitMQ用来创建订单，通过消息队列异步操作，在用户秒杀到商品的时候可以直接给用户返回“订单成功”，然后将数据放入消息队列中，后台线程再去队列里拿消息消费进行订单的创建，这样就不需要等到具体的订单创建好再返回。

#### 测试

这里我用的是ab(Apache压力测试工具)进行压测，-n 发出100个请求 -c100个并发 模拟100个人同时访问

测试结果如图：（项目目录下的两张图片）

![ab压测结果](.\ab压测结果.png)

![console输出](.\console输出.png)

