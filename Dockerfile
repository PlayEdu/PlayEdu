FROM openjdk:17
MAINTAINER 杭州白书科技有限公司 "tengyongzhi@meedu.vip"

# 使用东八区时间环境
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 将指定目录下的jar包复制到docker容器的/目录下
COPY /target/*.jar /app.jar

# 声明服务运行在8080端口
EXPOSE 9898

# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]
