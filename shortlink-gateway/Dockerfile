# 使用 openjdk 作为基础镜像
FROM openjdk:17-jdk-slim

# 将本地的 JAR 文件复制到容器内
COPY ./target/shortlink-gateway-1.0-SNAPSHOT.jar /app/shortlink-gateway-1.0-SNAPSHOT.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "/app/shortlink-gateway-1.0-SNAPSHOT.jar"]

# 开放应用程序运行的端口8080
EXPOSE 8080


