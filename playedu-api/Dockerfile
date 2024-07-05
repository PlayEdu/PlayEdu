FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS builder

COPY . /app

WORKDIR /app

RUN /app/mvnw -Dmaven.test.skip=true clean package

FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS base

WORKDIR /app

COPY --from=builder /app/playedu-api/target/playedu-api.jar /app/app.jar

RUN chmod +x /app/app.jar

EXPOSE 9898/tcp

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
