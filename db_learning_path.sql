-- 学习路径表
CREATE TABLE `learning_paths` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '路径标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径描述',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '学习路径表';

-- 学习路径节点表
CREATE TABLE `learning_path_nodes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `path_id` int(11) NOT NULL DEFAULT 0 COMMENT '路径ID',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '节点类型[1:课程,2:考试]',
  `target_id` int(11) NOT NULL DEFAULT 0 COMMENT '目标ID(课程ID或考试ID)',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_path_id` (`path_id`),
  KEY `idx_type` (`type`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '学习路径节点表';

-- 学习路径部门关联表
CREATE TABLE `learning_path_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `path_id` int(11) NOT NULL DEFAULT 0 COMMENT '路径ID',
  `dep_id` int(11) NOT NULL DEFAULT 0 COMMENT '部门ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_path_id` (`path_id`),
  KEY `idx_dep_id` (`dep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '学习路径部门关联表';

-- 用户学习路径记录表
CREATE TABLE `user_learning_path_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `path_id` int(11) NOT NULL DEFAULT 0 COMMENT '路径ID',
  `completed_nodes` int(11) NOT NULL DEFAULT 0 COMMENT '已完成节点数',
  `total_nodes` int(11) NOT NULL DEFAULT 0 COMMENT '总节点数',
  `progress` int(11) NOT NULL DEFAULT 0 COMMENT '进度',
  `is_finished` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否完成[1:是,0:否]',
  `finished_at` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_path_id` (`path_id`),
  KEY `idx_is_finished` (`is_finished`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户学习路径记录表';

-- 用户学习路径节点记录表
CREATE TABLE `user_learning_path_node_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `path_id` int(11) NOT NULL DEFAULT 0 COMMENT '路径ID',
  `node_id` int(11) NOT NULL DEFAULT 0 COMMENT '节点ID',
  `is_completed` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否完成[1:是,0:否]',
  `completed_at` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_path_id` (`path_id`),
  KEY `idx_node_id` (`node_id`),
  KEY `idx_is_completed` (`is_completed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户学习路径节点记录表';

-- 学习计划表
CREATE TABLE `learning_plans` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '计划标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '计划描述',
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始日期',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '结束日期',
  `progress` int(11) NOT NULL DEFAULT 0 COMMENT '进度',
  `is_finished` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否完成[1:是,0:否]',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_finished` (`is_finished`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '学习计划表';

-- 学习计划任务表
CREATE TABLE `learning_plan_tasks` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plan_id` int(11) NOT NULL DEFAULT 0 COMMENT '计划ID',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '任务类型[1:课程,2:考试,3:路径]',
  `target_id` int(11) NOT NULL DEFAULT 0 COMMENT '目标ID',
  `deadline` timestamp NULL DEFAULT NULL COMMENT '截止时间',
  `is_completed` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否完成[1:是,0:否]',
  `completed_at` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_type` (`type`),
  KEY `idx_target_id` (`target_id`),
  KEY `idx_is_completed` (`is_completed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '学习计划任务表';
