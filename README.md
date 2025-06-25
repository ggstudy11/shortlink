## 项目简介
这是一个基于Spring Boot和Spring Cloud的短链接服务项目，包含管理后台、网关和核心业务模块，支持短链接的生成、管理和访问。访问地址:http://49.234.192.196

## 模块说明
- shortlink-admin ：管理后台模块，提供短链接管理和系统配置功能。
- shortlink-gateway ：网关模块，负责请求路由和负载均衡。
- shortlink-project ：核心业务模块，处理短链接的生成、解析及存储。
## 技术栈
- Spring Boot
- Spring Cloud Gateway
- MyBatis-Plus
- Redis
- ShardingSphere
- Fastjson2
- Docker & Docker Compose
## 构建与运行
1. 使用Maven构建项目： mvn clean install
2. 使用Docker构建镜像并运行容器，项目根目录下有 docker-compose.yml 文件支持多容器部署。
## 目录结构
```
shortlink-admin/ #管理后台模块
shortlink-gateway/ #网关模块
shortlink-project/ #核心业务模块
```

**由GPT-4o编写，将于2025.7月补充**
