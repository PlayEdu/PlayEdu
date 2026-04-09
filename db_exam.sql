-- 考试表
CREATE TABLE `exams` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '考试标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '考试描述',
  `duration` int(11) NOT NULL DEFAULT 0 COMMENT '考试时长(分钟)',
  `pass_score` int(11) NOT NULL DEFAULT 60 COMMENT '及格分数',
  `total_score` int(11) NOT NULL DEFAULT 100 COMMENT '总分',
  `is_show` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否显示[1:是,0:否]',
  `is_required` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否必修[1:是,0:否]',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建管理员ID',
  PRIMARY KEY (`id`),
  KEY `idx_is_show` (`is_show`),
  KEY `idx_is_required` (`is_required`),
  KEY `idx_admin_id` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '考试表';

-- 考试题目表
CREATE TABLE `exam_questions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` int(11) NOT NULL DEFAULT 0 COMMENT '考试ID',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '题目类型[1:单选题,2:多选题,3:判断题,4:简答题]',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目内容',
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '选项( JSON格式 )',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '答案',
  `score` int(11) NOT NULL DEFAULT 10 COMMENT '分值',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '考试题目表';

-- 考试部门关联表
CREATE TABLE `exam_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` int(11) NOT NULL DEFAULT 0 COMMENT '考试ID',
  `dep_id` int(11) NOT NULL DEFAULT 0 COMMENT '部门ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_dep_id` (`dep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '考试部门关联表';

-- 考试分类关联表
CREATE TABLE `exam_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_id` int(11) NOT NULL DEFAULT 0 COMMENT '考试ID',
  `category_id` int(11) NOT NULL DEFAULT 0 COMMENT '分类ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '考试分类关联表';

-- 用户考试记录表
CREATE TABLE `user_exam_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `exam_id` int(11) NOT NULL DEFAULT 0 COMMENT '考试ID',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT '得分',
  `is_pass` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否通过[1:是,0:否]',
  `used_time` int(11) NOT NULL DEFAULT 0 COMMENT '用时(分钟)',
  `answers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '答案( JSON格式 )',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_is_pass` (`is_pass`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户考试记录表';

-- 证书表
CREATE TABLE `certificates` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `exam_id` int(11) NOT NULL DEFAULT 0 COMMENT '考试ID',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT '得分',
  `issue_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '颁发日期',
  `expire_date` timestamp NULL DEFAULT NULL COMMENT '过期日期',
  `certificate_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '证书编号',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_certificate_no` (`certificate_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '证书表';
