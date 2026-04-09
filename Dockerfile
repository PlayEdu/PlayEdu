FROM registry.cn-hangzhou.aliyuncs.com/hzbs/node:20-alpine AS node-builder

COPY playedu-admin /app/admin
COPY playedu-pc /app/pc
COPY playedu-h5 /app/h5

WORKDIR /app/admin
RUN pnpm i && VITE_APP_URL=/api/ pnpm build

WORKDIR /app/pc
RUN pnpm i && VITE_APP_URL=/api/ pnpm build

WORKDIR /app/h5
RUN pnpm i && VITE_APP_URL=/api/ pnpm build

FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS java-builder

COPY playedu-api /app

WORKDIR /app

# 使用国内 Maven 镜像源
RUN mkdir -p /root/.m2 && echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd"><mirrors><mirror><id>aliyun</id><name>Aliyun Maven</name><url>https://maven.aliyun.com/repository/public</url><mirrorOf>*</mirrorOf></mirror></mirrors></settings>' > /root/.m2/settings.xml

RUN /app/mvnw -Dmaven.test.skip=true clean package

FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS base

COPY --from=java-builder /app/playedu-api/target/playedu-api.jar /app/api/app.jar

COPY --from=node-builder /app/admin/dist /app/admin
COPY --from=node-builder /app/pc/dist /app/pc
COPY --from=node-builder /app/h5/dist /app/h5

COPY docker/nginx/conf/nginx.conf /etc/nginx/sites-enabled/default

EXPOSE 9898
EXPOSE 9800
EXPOSE 9801
EXPOSE 9900

CMD nginx; echo "Waiting for MySQL to start..."; sleep 15; java -jar /app/api/app.jar --spring.profiles.active=prod --spring.datasource.url="jdbc:mysql://${DB_HOST}:${DB_PORT:-3306}/${DB_NAME}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true" --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASS} --sa-token.is-concurrent=${SA_TOKEN_IS_CONCURRENT:-false} --sa-token.jwt-secret-key=${SA_TOKEN_JWT_SECRET_KEY}