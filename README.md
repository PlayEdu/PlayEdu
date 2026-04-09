<p align="center">
<img src="https://meedu.cloud.oss.meedu.vip/playedu/%E5%A4%B4%E5%9B%BE.jpg"/>
</p>

<h4 align="center">
  <a href="https://github.com/Tingfe/PlayEdu">GitHub仓库</a> |
  <a href="http://www.playeduos.com">PlayEdu官网</a> |
  <a href="https://faq.playeduos.com/opensource-maintenance-handbook/article/t08o2iHfLR">部署文档</a>
</h4>

## 项目简介

PlayEdu 是一款业内领先的线上培训解决方案，基于 Java + MySQL 开发，采用前后端分离模式，前端核心框架为 React18，后端核心框架为 SpringBoot3。

本项目是基于原 PlayEdu 开源版的增强版本，添加了更多企业级功能和性能优化。

### 核心功能

- **部门管理**：支持部门层级管理，实现组织架构的完整映射
- **学员管理**：支持学员信息管理、批量导入、学习进度追踪
- **在线视频学习**：支持视频课程上传、章节管理、课时安排
- **学员进度追踪**：实时监控学员学习状态和进度
- **视频私有化存储**：支持阿里云OSS和腾讯云COS存储
- **LDAP集成**：支持LDAP用户同步和登录

### ✨ 新增功能

- **培训分析**：提供培训数据统计和分析功能，帮助企业了解培训效果和学员学习情况
- **课程管理优化**：调整课程管理页面代码结构，提升系统性能和用户体验
- **依赖升级**：升级pnpm lockfile版本，确保依赖管理的稳定性

**针对企业级培训场景，我们精心打造了"功能更多、响应更快、并发更强"的企业版本，满足企业多样化的培训需求。企业版本支持音视频学习、文档在线预览、线上考试、学习任务等多种学习方式，并提供多重安全防护，如视频转码加密、防盗链、学习防快进、防挂机等。同时，我们集成了企业微信、钉钉、飞书等主流办公系统，帮助企业快速部署专属培训平台！**

## 技术栈

### 前端
- React 18
- TypeScript
- Ant Design 5
- Redux Toolkit
- Vite
- ECharts

### 后端
- Spring Boot 3.3.4
- Java 17
- MyBatis-Plus
- MySQL
- AWS S3 SDK (支持阿里云OSS和腾讯云COS)
- Sa-Token (认证授权)
- Redis (可选)

## 🚀 快速上手

### 拉取代码

```bash
git clone --branch main https://github.com/Tingfe/PlayEdu.git playedu
```

### 构建镜像

```bash
cd playedu && docker-compose up -d
```

### 访问地址

命令执行完成以后，打开您的浏览器，输入以下地址访问对应服务：

- **后台管理**：`http://localhost:9900` (默认管理员账号：admin@playedu.xyz / 密码：playedu)
- **PC学员端**：`http://localhost:9800`
- **H5学员端**：`http://localhost:9801`
- **API服务**：`http://localhost:9700`

## 系统架构

PlayEdu 采用前后端分离的微服务架构，主要包含以下模块：

- **playedu-admin**：后台管理系统前端
- **playedu-pc**：PC端学员系统前端
- **playedu-h5**：H5端学员系统前端
- **playedu-api**：API网关服务
- **playedu-common**：公共模块
- **playedu-system**：系统管理模块
- **playedu-course**：课程管理模块
- **playedu-resource**：资源管理模块

## 主要功能模块

### 1. 后台管理系统
- **系统配置**：全局系统设置、存储配置、LDAP配置
- **部门管理**：部门层级管理、学员分配
- **学员管理**：学员信息管理、批量导入、学习进度查看
- **课程管理**：课程创建、章节管理、课时安排、学员分配
- **资源管理**：视频、图片等资源上传和管理
- **角色权限**：基于角色的权限控制

### 2. 学员系统
- **课程学习**：在线视频学习、学习进度保存
- **学习记录**：查看历史学习记录和进度
- **个人中心**：个人信息管理

### 3. API服务
- **认证授权**：JWT token认证
- **课程管理API**：课程相关操作接口
- **学员管理API**：学员相关操作接口
- **资源管理API**：资源上传和访问接口
- **LDAP集成**：LDAP用户同步和认证

## 🔰️ 软件安全

安全问题应该通过邮件私下报告给 tengyongzhi@meedu.vip。 您将在 24 小时内收到回复，如果因为某些原因您没有收到回复，请通过回复原始邮件的方式跟进，以确保我们收到了您的原始邮件。

## 👁 界面预览

![学员端口界面预览](https://meedu.cloud.oss.meedu.vip/playedu/%E5%89%8D%E5%8F%B0%E9%A1%B5%E9%9D%A2.jpg)

![管理后台界面预览](https://meedu.cloud.oss.meedu.vip/playedu/%E5%90%8E%E5%8F%B0%E9%A1%B5%E9%9D%A2.jpg)

## 📃 使用须知

- **1.版权归属**： 杭州白书科技有限公司对 PlayEdu 开源版拥有完整版权，所有使用权保留。
- **2.代码修改**： 在遵守相关开源协议的严格前提下，允许对 PlayEdu 开源版代码进行修改。修改时，必须在代码中加入明确备注，详细记录每一处修改的具体内容。
- **3.版权保护**： 严令禁止删除、修改或篡改源代码中的版权信息及开源说明文件，侵犯版权的行为将面临法律追究。
- 在任何使用场景下，必须严格保留 PlayEdu 开源版页面及代码中的原有版权信息，包括不限于 "Designed By PlayEdu" 页面版权标识、官网链接以及代码中的开源说明等，一旦出现侵犯版权的行为，将承担相应法律责任。

## 开源协议

PlayEdu 开源版采用 [Apache License 2.0](https://opensource.org/licenses/Apache-2.0) 协议。

## 贡献指南

我们欢迎社区贡献，包括但不限于：
- 提交 bug 报告
- 提出功能建议
- 提交代码 PR
- 改进文档

## 联系我们

- **GitHub仓库**：[https://github.com/Tingfe/PlayEdu](https://github.com/Tingfe/PlayEdu)
- **技术交流**：tengyongzhi@meedu.vip