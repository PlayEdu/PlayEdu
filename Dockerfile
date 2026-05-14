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

CMD nginx; echo "Waiting for MySQL to start..."; sleep 15; java -jar /app/api/app.jar