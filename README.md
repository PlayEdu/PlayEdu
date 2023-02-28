### 概述

**PlayEdu** 是基于 Java + SpringBoot3.0 开发的开源的企业、机构、大学院校的内部培训解决方案。

### 开始上手

使用 `docker` 安装 `minio` :

> 下面是测试环境安装的 minio .

```
docker run -p 9000:9000 -p 50000:50000 -d --name playedu-minio \
  -e "MINIO_ACCESS_KEY=username" \
  -e "MINIO_SECRET_KEY=password" \
  minio/minio server --console-address ":50000" /data
```

如果您想在生产环境中使用，推荐您做好数据卷的挂载并自行配置 `AccessKey` 和 `SecretKey` :

```
docker run -p 9000:9000 -p 50000:50000 -d --name playedu-minio \
  -e "MINIO_ACCESS_KEY=自定义用户名" \
  -e "MINIO_SECRET_KEY=自定义密码" \
  -v /mnt/data:/data \
  -v /mnt/config:/root/.minio \
  minio/minio server --console-address ":50000" /data
```

### 使用协议

欢迎使用杭州白书科技有限公司提供的开源培训解决方案！请您仔细阅读以下条款。通过使用 PlayEdu ，您表示同意接受以下所有条款。

+ 本开源项目中所有代码基于 Apache-2.0 许可协议，您默认遵守许可协议中约定的义务。
+ 您默认授权我们将您使用 PlayEdu 所在业务的 Logo 放置在本官网展示。