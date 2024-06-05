<p align="center">
<img src="https://meedu.cloud.oss.meedu.vip/playedu/%E5%A4%B4%E5%9B%BE.jpg"/>
</p>

<h4 align="center">
  <a href="http://www.playeduos.com">官网</a> |
  <a href="https://www.playeduos.com/function.html">商业版</a> |
  <a href="https://faq.playeduos.com/">部署文档</a> |
  <a href="https://www.playeduos.com/demo.html">演示站</a> |
  <a href="https://faq.playeduos.com/qa">开源社区</a>
</h4>

## 系统介绍

**PlayEdu** 是由白书科技团队经营多年线上教育系统打造出的一款全新的企业培训解决方案，致力于为更多企业机构搭建私有化内部培训平台。**PlayEdu** 基于 Java + MySQL 开发；采用前后端分离模式；前端采用 React18 为核心框架，后端采用 SpringBoot3 为核心框架。提供部门管理、学员管理、在线视频学习、学员进度追踪、视频私有化存储等培训主要功能。**与此同时，我们在开源版本的基础上还提供了功能更加丰富的企业版本。企业版本在开源功能的基础上提供了包括视频云端存储、视频加密、音频学习、文档(PDF|WORD|PPT)在线学习、在线考试、学习计划培训等功能。企业版本更多信息请点击下方的企业版链接查看。**

## 常用链接

| 站点       | 链接                                                                     |
| ---------- | ------------------------------------------------------------------------ |
| 官网       | [http://www.playedu.xyz](http://www.playedu.xyz)                         |
| **企业版** | [https://www.playedu.xyz/commercial](https://www.playedu.xyz/commercial) |
| 部署文档   | [https://www.playedu.xyz/book](https://www.playedu.xyz/book)             |
| 系统演示   | [https://www.playedu.xyz/demo](https://www.playedu.xyz/demo)             |
| 问答社区   | [https://www.playedu.xyz/qa](https://www.playedu.xyz/qa)                 |

## 🚀 快速上手

拉取代码：

```
git clone --branch main https://gitee.com/playeduxyz/playedu.git playedu
```

构建镜像：

```
cd playedu && docker-compose build
```

运行镜像：

```
docker-compose up -d
```

现在，打开您的浏览器，输入 `http://localhost:9900` 即可访问后台管理界面，默认管理员账号和密码 `admin@playedu.xyz / playedu` 。

- PC 端口 `http://localhost:9800`
- H5 端口 `http://localhost:9801`
- API 端口 `http://localhost:9700`

## 🔰️ 软件安全

安全问题应该通过邮件私下报告给 tengyongzhi@meedu.vip。 您将在 24 小时内收到回复，如果因为某些原因您没有收到回复，请通过回复原始邮件的方式跟进，以确保我们收到了您的原始邮件。

## 界面预览

![学员端口界面预览](https://meedu.cloud.oss.meedu.vip/playedu/%E5%89%8D%E5%8F%B0%E9%A1%B5%E9%9D%A2.jpg)

![管理后台界面预览](https://meedu.cloud.oss.meedu.vip/playedu/%E5%90%8E%E5%8F%B0%E9%A1%B5%E9%9D%A2.jpg)

## 使用须知

- **1.版权归属**： PlayEdu 开源版版权归杭州白书科技有限公司所有。我们鼓励社区成员在遵守开源协议的前提下，对开源版进行合法的使用和贡献。
- **2.代码修改**： 用户有权在遵守开源协议的前提下，对 PlayEdu 开源版的代码进行必要的修改和优化。需在修改处添加清晰备注，记录修改内容信息。
- **3.版权保护**： 严禁删除、修改或篡改源代码中的版权信息及开源说明文件。
- **4.商业化行为**： 在遵循开源协议的前提下，可以进行商用。但请注意，任何商业化行为都应明确标注 PlayEdu 开源版的版权信息。
