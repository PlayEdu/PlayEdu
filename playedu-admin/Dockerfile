FROM node:lts-slim as builder

WORKDIR /app

COPY . /app

RUN yarn config set registry https://registry.npm.taobao.org && yarn && yarn build

FROM nginx:1.23.4-alpine-slim

COPY --from=builder /app/dist /usr/share/nginx/html

COPY --from=builder /app/docker/nginx.conf /etc/nginx/nginx.conf