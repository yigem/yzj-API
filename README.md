## API接口开放平台

### 项目简介

提供 API **接口供开发者调用的平台；**

流程：有用户前台和后端管理系统，管理员可以在后端实现发布接口，下线接口，查看用户调用接口的情况。用户可以浏览接口，开通接口的调用权限和调用接口。除此之外，为了防止用户直接调用数据库信息和管理员无法了解用户的调用情况，所以需要通过 API 网关对这些调用接口进行 **统一的保护**，集中的去做用户调用接口的统计次数和鉴权等功能，因为没有对接接口，所以要模拟接口，来测试用户对接口的调用。使用客户端 SDK 轻松在代码中进行调用；

这个不是接口管理平台，接口管理平台是内部自己开发的接口，可以用swagger或knife4j接口文档**来给别人看到**。**API 开发平台是开发给别人用的**

业务流程：

![image](https://github.com/yigem/yzj-API/blob/main/tupian/yewuliucheng.png) 

#### 技术选型

###### 前端

 

●Ant Design Pro 

●React

●Ant Design Procomponents

●Umi

●Umi Request（Axios 的封装）

 

###### 后端

●Java Spring Boot

●Spring Boot Starter（SDK 开发）

●Dubbo（RPC）

●Nacos

●Spring Cloud Gateway（网关、限流、日志实现）

#### 项目脚手架

前端：ant design pro 脚手架（https://pro.ant.design/zh-CN/）

后端：CRUD代码模板



#### 模拟接口项目

因为没有对接接口，所以要有模拟接口，用来测试用户对接口的调用

项目名称：yzj-interface

提供三个不同种类的模拟接口：

1. GET接口
2. POST接口（url传参）
3. POST接口（Restful）

##### 调用接口选择第三方库（Hutool）

Hutool：https://hutool.cn/docs/#/
Http 工具类：https://hutool.cn/docs/#/http/Http%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B7%A5%E5%85%B7%E7%B1%BB-HttpUtil



#### API签名认证

**1.保证安全性，不能随便一个人调用**

2.适用于无需保存登录态的场景。只认签名，不关注用户登录态。

##### 签名认证的实现

通过 http request header 头传递参数。

参数1：accessKey：调用的标识（复杂，无序，无规律）

参数2：secretKey：密钥（复杂，无序，无规律）**该参数不能放到请求头中**

（类似用户名和密码，区别：ak，sk是无状态的）

**千万不能把密钥直接在服务器之间传递，有可能会被拦截**

参数3：用户请求参数

参数4：sign

加密方式：对称加密，非对称加密，**md5签名（不可解密）**

用户参数 + 密钥 ==> 签名生成算法（MD5，HMac，Sha1）==> 不可解密的值

如：abc + 密钥（abcdef） ==> sdahjklshd

**服务端如何知道这个签名对不对？**

服务端用一模一样的参数和算法生成签名，只要和用户传的一致，就表示一致。

**怎么防重放？**

参数5：加 nonce 随机数，只能用一次

服务端要保存用过的随机数

参数6：加 timestamp 时间戳，检验时间戳是否过期

**API签名认证是一个很灵活的设计，具体要有哪些参数，参数名如何一定要根据场景来。（比如 userId等）**



**难道开发者每次调用接口都要自己写签名算法嘛？**

开发一个简单易用的SDK

项目名：yzj-client-sdk

开发 Starter

**为什么需要Starter？**

理想情况：开发者只需要关心调用哪些接口，传递哪些参数，就跟调用自己写的代码一样简单。

开发 starter 的好处：开发者引入之后，可以直接在 application.yml 中写配置，自动创建客户端



**调用接口**

使用如图所示流程：走后端调用

![image](https://github.com/yigem/yzj-API/blob/main/tupian/diaoyongjiekou.png) 

流程：

1.前端将用户输入的请求参数和要测试的接口id发给平台后端

2.（在调用前可以做一些校验）

3.平台后端去调用模拟接口

**优化整个系统的架构---API 网关**（选用 GateWay）

首先要实现接口调用次数的统计，但是有一个问题，每一次调用接口的方法都要写调用次数 + 1，比较麻烦！

致命问题：接口开发者需要自己去添加统计代码！

可以加个 API 网关，如下图所示

![image-20230604191717980](C:\Users\bx\AppData\Roaming\Typora\typora-user-images\image-20230604191717980.png)

也可以用 AOP，但AOP 切面的缺点：只存在于单个项目中，如果每个团队都要开发自己的模拟接口，那么都要写一个切面。

优点：独立于接口，在每个接口调用后统计次数 + 1

##### **然后实现统一的接口鉴权和计费（API 网关实践）**

需要用到的 GateWay 特性：

1.路由（转发请求到模拟接口项目）

2.统一鉴权（accesskey，secretkey）（在全局过滤器中）

3.统一业务处理（每次请求接口后，接口调用次数 + 1）

4.访问控制（黑白名单）

5.流量染色（记录请求是否为网关来的）

6.统一日志（记录每次的请求和响应日志）

**全局过滤器的业务逻辑**

1.用户发送请求到 API 网关

2.请求日志

3.黑白名单

4.用户鉴权（判断ak，sk 是否合法）

5.请求的模拟接口是否存在？

**6.请求转发，调用模拟接口**（在application.yml中实现，第一步先进行调用，确定能调用成功之后再执行其他业务）

7.响应日志

8.调用成功，接口调用次数 + 1

9.调用失败，返回一个规范的错误码

**但是执行过程中发现一个问题：**

预期是等模拟接口调用完成后，才记录响应日志，统计调用次数。

但现实是 chain.filter 方法立刻返回了，直到 filter 过滤器 return 后才调用了模拟接口。

**原因是：**chain.filter 是一个异步操作。

**解决方案：**利用 response 装饰者，增强原有 response 的处理能力

参考博客：**https://blog.csdn.net/qq_19636353/article/details/126759522**

##### 实现统一的接口鉴权和计费（RPC、Dubbo 讲解）

**网关业务逻辑**

问题：网关项目比较纯净，没有操作数据库的包，而且还要调用之前写过的代码（调用不同项目的代码），复制粘贴维护很麻烦；

理想情况：直接请求到其他项目的方法

**有四种调用其他项目的方法？**

1.复制代码和依赖，环境

2.HTTP请求（提供一个接口，供其他项目调用）

3.RPC（这个项目使用RPC来完成调用）

4.把公共的代码打个jar包，其他项目去引用（客户端SDK）

##### RPC（通过Dubbo来实现）

可以阅读官方文档来了解 Dubbo
https://dubbo.incubator.apache.org/zh/docs3-v2/java-sdk/quick-start/spring-boot/

作用：**像调用本地方法一样调用远程方法**

和直接 HTTP 调用的区别：

1.对开发者更透明，减少了很多的沟通成本

2.RPC向远程服务器发送请求时，为必要使用HTTP协议，比如还可以用 TCP/IP，性能更高。

**RPC调用模型：**

![image](https://github.com/yigem/yzj-API/blob/main/tupian/RPCdiaoyong.png) 

建议大家用 Nacos！
整合 Nacos 注册中心：https://dubbo.apache.org/zh/docs3-v2/java-sdk/reference-manual/registry/nacos/

依赖：

```xml
<!-- https://mvnrepository.com/artifact/org.apache.dubbo/dubbo -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>3.0.9</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>2.1.0</version>
        </dependency>
```

#### 抽象公共服务

项目名：yzj-common

目的是让方法，实体类在多个项目间复用，减少重复编写。

步骤：
① 新建干净的 maven 项目，只保留必要的公共依赖

② 抽取 service 和 实体类

③ install 本地 maven 包

④ 让服务提供者引入 common包，测试是否正常运行

⑤ 让服务消费者引入 common 包。 
