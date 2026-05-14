FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS builder

WORKDIR /app

COPY mvnw          /app/mvnw
COPY .mvn          /app/.mvn
COPY pom.xml                    /app/pom.xml
COPY playedu-api/pom.xml        /app/playedu-api/pom.xml
COPY playedu-common/pom.xml     /app/playedu-common/pom.xml
COPY playedu-course/pom.xml     /app/playedu-course/pom.xml
COPY playedu-resource/pom.xml   /app/playedu-resource/pom.xml
COPY playedu-system/pom.xml     /app/playedu-system/pom.xml

RUN /app/mvnw -B -DskipTests dependency:go-offline

COPY . /app

RUN /app/mvnw -B -Dmaven.test.skip=true package

FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS base

WORKDIR /app

COPY --from=builder /app/playedu-api/target/playedu-api.jar /app/app.jar

RUN chmod +x /app/app.jar

EXPOSE 9898/tcp

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
