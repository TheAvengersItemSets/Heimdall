

activemq-5.11.1
mysql-5.7.27
zookeeper-3.4.12

> [XxPay官网：http://www.xxpay.org](http://www.xxpay.org "xxpay官方网站")

> [XxPay开发社区：http://pub.xxpay.org](http://pub.xxpay.org "xxpay开发社区")

> [XxPay统一扫码支付体验：http://shop.xxpay.org/goods/openQrPay.html](http://shop.xxpay.org/goods/openQrPay.html "xxpay支付体验")

> [XxPay运营平台演示：http://mgr.xxpay.org](http://mgr.xxpay.org "xxpay运营平台")

> [XxPay文档库：http://docs.xxpay.org](http://docs.xxpay.org "xxpay文档库")


#### xxpay-master
| 项目  | 端口 | 描述
|---|---|---
|xxpay-common |  | 公共模块(常量、工具类等)，jar发布
|xxpay-dal |  | 支付数据持久层，jar发布
|xxpay-mgr | 8092 | 支付运营平台
|xxpay-shop | 8081 | 支付商城演示系统
|xxpay4spring-cloud |  | 支付中心spring-cloud架构实现
#### xxpay4spring-cloud
| 项目  | 端口 | 描述
|---|---|---
|xxpay-config | 2020 | 支付服务配置中心
|xxpay-gateway | 3020 | 支付服务API网关
|xxpay-server | 2000 | 支付服务注册中心
|xxpay-service | 3000 | 支付服务生产者
|xxpay-web | 3010 | 支付服务消费者
项目启动顺序：
```
xxpay-server > xxpay-config > xxpay-service > xxpay-web > xxpay-gateway
```
