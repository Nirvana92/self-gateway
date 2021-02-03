自定义的网关模块

模块分为如下几个模块: 
self-gateway-common: 公共的模块, 一些常量, 公用的pojo
self-gateway-server: 真正的服务模块
self-gateway-springboot-starter: 自动装配的启动类
self-gateway-admin: 需要进行后台配置网络路由的后台服务
self-gateway-rpc: 通信模块[待处理, 可以考虑抽离出另外一个模块]

网关服务高可用的使用: 将多个网关服务注册到服务注册中心即可保证网关的高可用
