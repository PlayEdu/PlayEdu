FROM openjdk:17-slim

ENV LANG en_US.UTF-8

RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

WORKDIR /app

# 使用东八区时间环境
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 将指定目录下的jar包复制到docker容器的/目录下
COPY /target/playedu-api-*.jar /app/app.jar

# 声明服务运行在8080端口
EXPOSE 9898

# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]
