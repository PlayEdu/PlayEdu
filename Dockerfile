FROM registry.cn-hangzhou.aliyuncs.com/hzbs/node:20-alpine AS node-builder

WORKDIR /app

COPY playedu-admin/package.json playedu-admin/pnpm-lock.yaml /app/admin/
COPY playedu-pc/package.json    playedu-pc/pnpm-lock.yaml    /app/pc/
COPY playedu-h5/package.json    playedu-h5/pnpm-lock.yaml    /app/h5/

RUN cd /app/admin && pnpm i
RUN cd /app/pc    && pnpm i
RUN cd /app/h5    && pnpm i

COPY playedu-admin /app/admin
RUN cd /app/admin && VITE_APP_URL=/api/ pnpm build

COPY playedu-pc /app/pc
RUN cd /app/pc && VITE_APP_URL=/api/ pnpm build

COPY playedu-h5 /app/h5
RUN cd /app/h5 && VITE_APP_URL=/api/ pnpm build

FROM registry.cn-hangzhou.aliyuncs.com/hzbs/eclipse-temurin:17 AS java-builder

WORKDIR /app

COPY playedu-api/mvnw          /app/mvnw
COPY playedu-api/.mvn          /app/.mvn
COPY playedu-api/pom.xml                     /app/pom.xml
COPY playedu-api/playedu-api/pom.xml         /app/playedu-api/pom.xml
COPY playedu-api/playedu-common/pom.xml      /app/playedu-common/pom.xml
COPY playedu-api/playedu-course/pom.xml      /app/playedu-course/pom.xml
COPY playedu-api/playedu-resource/pom.xml    /app/playedu-resource/pom.xml
COPY playedu-api/playedu-system/pom.xml      /app/playedu-system/pom.xml

RUN /app/mvnw -B -DskipTests dependency:go-offline

COPY playedu-api /app

RUN /app/mvnw -B -Dmaven.test.skip=true package

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
