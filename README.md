## 项目简介
**短链接系统**，为企业和个人用户提供了一个高效、安全和可靠的短链接管理平台。
该平台不仅简化了长链接的管理和分享过程，还提供了深入的分析和跟踪功能，用户可以灵活地管理和优化其链接，从而实现更好的营销效果和业务成果。

访问地址: http://49.234.192.196

用户名：admin   
密码：admin123456

暂未开放注册接口，仅用于演示与测试。

## 模块说明
- shortlink-admin ：管理后台模块，提供短链接管理和系统配置功能。
- shortlink-gateway ：网关模块，负责请求路由和负载均衡。
- shortlink-project ：核心业务模块，处理短链接的生成、解析及存储。
## 技术栈
- Spring Boot
- Spring Cloud Alibaba
- MyBatis-Plus
- Redis
- ShardingSphere
- RabbitMQ
- Sentinel
- Docker
## 构建与运行
1. 使用Maven构建项目： mvn package
2. 使用Docker构建镜像并运行容器，项目根目录下有 docker-compose.yml 文件支持多容器部署。
## 目录结构
```
shortlink-admin/ #管理后台模块
shortlink-gateway/ #网关模块
shortlink-project/ #核心业务模块
```
## 接口文档
[接口文档](./doc/接口文档.md)
## 系统架构
[系统架构](./doc/系统架构.md)

