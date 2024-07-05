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

CMD nginx; echo "Waiting for MySQL/Redis/MinIO to start..."; sleep 15; java -jar /app/api/app.jar --spring.profiles.active=prod --spring.datasource.url="jdbc:mysql://${DB_HOST}:${DB_PORT:-3306}/${DB_NAME}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true" --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASS} --spring.data.redis.host=${REDIS_HOST} --spring.data.redis.port=${REDIS_PORT:-6379} --spring.data.redis.password=${REDIS_PASS} --spring.data.redis.database=${REDIS_DB:-0} --sa-token.is-concurrent=${SA_TOKEN_IS_CONCURRENT:-false} --sa-token.jwt-secret-key=${SA_TOKEN_JWT_SECRET_KEY}