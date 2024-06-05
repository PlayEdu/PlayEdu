FROM eclipse-temurin:17 as builder

COPY . /app

WORKDIR /app

RUN /app/mvnw -Dmaven.test.skip=true clean package

FROM eclipse-temurin:17

WORKDIR /app

# 使用东八区时间环境
RUN echo "Asia/Shanghai" > /etc/timezone

# 将指定目录下的jar包复制到docker容器的/目录下
COPY --from=builder /app/playedu-api/target/playedu-api.jar /app/app.jar

RUN chmod +x /app/app.jar

# 声明服务运行在8080端口
EXPOSE 9898

# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
