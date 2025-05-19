# LDAP同步记录功能

本文档介绍了PlayEdu系统中LDAP同步记录功能的使用方法和实现细节。

## 功能概述

LDAP同步记录功能主要实现了以下功能：

1. 记录每次LDAP数据同步的统计数据：
   - 同步的部门和用户总数
   - 新增、更新、删除的部门和用户数量
2. 将同步的LDAP数据保存到S3存储中，方便后续查询和下载
3. 记录执行同步操作的管理员ID
4. 通过状态控制防止短时间内多次提交同步请求
5. 记录每次同步中每个部门和用户的详细变更情况

## 数据库表

### 主同步记录表

系统添加了新的数据库表 `ldap_sync_record`，其结构如下：

```sql
CREATE TABLE `ldap_sync_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '执行同步的管理员ID，0表示系统自动执行',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0-进行中，1-成功，2-失败',
  `s3_file_path` varchar(255) DEFAULT NULL COMMENT 'S3存储中的文件路径',
  `total_department_count` int(11) NOT NULL DEFAULT 0 COMMENT '总部门数量',
  `created_department_count` int(11) NOT NULL DEFAULT 0 COMMENT '新增部门数量',
  `updated_department_count` int(11) NOT NULL DEFAULT 0 COMMENT '更新部门数量',
  `deleted_department_count` int(11) NOT NULL DEFAULT 0 COMMENT '删除部门数量',
  `total_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '总用户数量',
  `created_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '新增用户数量',
  `updated_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '更新用户数量',
  `deleted_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '删除用户数量',
  `banned_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '被禁止的用户数量',
  `error_message` text DEFAULT NULL COMMENT '错误信息',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='LDAP同步记录表';
```

### 部门同步详情表

记录每个部门在同步过程中的详细变更情况：

```sql
CREATE TABLE `ldap_sync_department_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_id` int(11) NOT NULL COMMENT '关联的同步记录ID',
  `department_id` int(11) DEFAULT NULL COMMENT '关联的部门ID',
  `uuid` varchar(255) NOT NULL COMMENT 'LDAP部门UUID',
  `dn` varchar(255) NOT NULL COMMENT 'LDAP部门DN',
  `name` varchar(255) NOT NULL COMMENT '部门名称',
  `action` tinyint(4) NOT NULL COMMENT '操作：1-新增，2-更新，3-删除，4-无变化',
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='LDAP部门同步详情表';
```

### 用户同步详情表

记录每个用户在同步过程中的详细变更情况：

```sql
CREATE TABLE `ldap_sync_user_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_id` int(11) NOT NULL COMMENT '关联的同步记录ID',
  `user_id` int(11) DEFAULT NULL COMMENT '关联的用户ID',
  `uuid` varchar(255) NOT NULL COMMENT 'LDAP用户UUID',
  `dn` varchar(255) NOT NULL COMMENT 'LDAP用户DN',
  `cn` varchar(255) NOT NULL COMMENT '用户名称',
  `uid` varchar(255) NOT NULL COMMENT '用户ID/登录名',
  `email` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `ou` text DEFAULT NULL COMMENT '用户部门路径',
  `action` tinyint(4) NOT NULL COMMENT '操作：1-新增，2-更新，3-删除，4-无变化，5-禁止',
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='LDAP用户同步详情表';
```

## API接口

系统提供了以下API接口用于LDAP同步操作和记录管理：

### 1. 手动触发LDAP同步

- **URL**: `/backend/v1/ldap/sync`
- **方法**: POST
- **权限**: `ldap:sync`
- **返回数据**:
  ```json
  {
    "code": 0,
    "data": {
      "record_id": 1 // 同步记录ID
    },
    "message": "success"
  }
  ```

### 2. 获取LDAP同步记录列表

- **URL**: `/backend/v1/ldap/sync-records`
- **方法**: GET
- **权限**: `ldap:sync:records`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10
- **返回数据**:
  ```json
  {
    "code": 0,
    "data": {
      "data": [
        {
          "id": 1,
          "admin_id": 0,
          "status": 1,
          "s3_file_path": "ldap/sync/ldap_sync_1_1620000000000.json",
          "total_department_count": 10,
          "created_department_count": 5,
          "updated_department_count": 3,
          "deleted_department_count": 2,
          "total_user_count": 100,
          "created_user_count": 50,
          "updated_user_count": 30,
          "deleted_user_count": 20,
          "banned_user_count": 0,
          "error_message": null,
          "created_at": "2023-08-01 12:00:00",
          "updated_at": "2023-08-01 12:01:00"
        }
      ],
      "total": 100
    },
    "message": "success"
  }
  ```

### 3. 获取LDAP同步记录详情

- **URL**: `/backend/v1/ldap/sync-records/{id}`
- **方法**: GET
- **权限**: `ldap:sync:records`
- **返回数据**:
  ```json
  {
    "code": 0,
    "data": {
      "id": 1,
      "admin_id": 0,
      "status": 1,
      "s3_file_path": "ldap/sync/ldap_sync_1_1620000000000.json",
      "total_department_count": 10,
      "created_department_count": 5,
      "updated_department_count": 3,
      "deleted_department_count": 2,
      "total_user_count": 100,
      "created_user_count": 50,
      "updated_user_count": 30,
      "deleted_user_count": 20,
      "banned_user_count": 0,
      "error_message": null,
      "created_at": "2023-08-01 12:00:00",
      "updated_at": "2023-08-01 12:01:00"
    },
    "message": "success"
  }
  ```

### 4. 获取同步详情

- **URL**: `/backend/v1/ldap/sync-records/{id}/details`
- **方法**: GET
- **权限**: `ldap:sync:records`
- **参数**:
  - `type`: 详情类型，`department`=部门，`user`=用户
  - `action`: 操作类型，当type=department时：0=全部，1=新增，2=更新，3=删除，4=无变化；当type=user时：0=全部，1=新增，2=更新，3=删除，4=无变化，5=禁止，默认0
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10
- **返回数据**:
  ```json
  {
    "code": 0,
    "data": {
      "records": [
        {
          // 当type=department时返回部门详情
          "id": 1,
          "record_id": 1,
          "department_id": 10,
          "uuid": "12345678-1234-1234-1234-123456789012",
          "dn": "ou=HR,dc=example,dc=com",
          "name": "HR",
          "action": 1,
          "created_at": "2023-08-01 12:00:00"
        }
        // 或当type=user时返回用户详情
        {
          "id": 1,
          "record_id": 1,
          "user_id": 100,
          "uuid": "12345678-1234-1234-1234-123456789012",
          "dn": "cn=John Doe,ou=HR,dc=example,dc=com",
          "cn": "John Doe",
          "uid": "johndoe",
          "email": "john.doe@example.com",
          "ou": "HR",
          "action": 1,
          "created_at": "2023-08-01 12:00:00"
        }
      ],
      "total": 100,
      "size": 10,
      "current": 1,
      "pages": 10
    },
    "message": "success"
  }
  ```

### 5. 下载LDAP同步记录数据

- **URL**: `/backend/v1/ldap/sync-records/{id}/download`
- **方法**: GET
- **权限**: `ldap:sync:records`
- **返回数据**:
  ```json
  {
    "code": 0,
    "data": {
      "url": "https://your-s3-domain.com/ldap/sync/ldap_sync_1_1620000000000.json"
    },
    "message": "success"
  }
  ```

## 定时同步

系统会每小时自动执行一次LDAP同步，同步过程和统计数据会记录到 `ldap_sync_record` 表中。自动同步时 `admin_id` 字段为0。

## 权限说明

为了使用LDAP同步记录功能，需要为管理员角色分配以下权限：

- `ldap:sync`: 允许手动触发LDAP同步
- `ldap:sync:records`: 允许查看LDAP同步记录

## 同步状态说明

LDAP同步记录的状态字段（`status`）有以下值：

- `0`: 进行中
- `1`: 成功
- `2`: 失败

当状态为2（失败）时，错误信息会记录在 `error_message` 字段中。

## 操作类型说明

详细同步记录中的操作类型（`action`）有以下值：

### 部门操作类型：
- `1`: 新增 - 首次在LDAP中发现的部门
- `2`: 更新 - 已存在但信息发生变化的部门
- `3`: 删除 - 在LDAP中不再存在的部门
- `4`: 无变化 - 已存在且信息未发生变化的部门

### 用户操作类型：
- `1`: 新增 - 首次在LDAP中发现的用户
- `2`: 更新 - 已存在但信息发生变化的用户
- `3`: 删除 - 在LDAP中不再存在的用户
- `4`: 无变化 - 已存在且信息未发生变化的用户
- `5`: 禁止 - 在LDAP中被标记为禁止状态的用户

## S3存储

同步的LDAP数据会以JSON格式保存到S3存储中，路径格式为：

```
ldap/sync/ldap_sync_{记录ID}_{时间戳}.json
```

JSON文件包含完整的同步数据，包括所有部门和用户的信息。

## 实现细节

系统在每次LDAP同步时都会进行以下操作：

1. 创建主同步记录，初始状态为"进行中"
2. 获取LDAP配置信息并查询所有LDAP部门和用户数据
3. 收集统计信息并保存到S3
4. 收集每个部门和用户的详细变更情况
5. 执行实际的同步操作，包括部门同步和用户同步
6. 保存部门和用户的详细变更记录
7. 更新主同步记录状态为"成功"

如果同步过程中出现异常，系统会捕获异常信息并更新主同步记录状态为"失败"。 