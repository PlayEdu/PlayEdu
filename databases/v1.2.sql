drop table admin_logs;

CREATE TABLE `admin_logs`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `admin_id`       int(11) NOT NULL DEFAULT '0' COMMENT '管理员ID',
    `module`         varchar(32) COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT '模块',
    `title`          varchar(32) COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT '请求方法标题',
    `opt`            int(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 0 COMMENT '操作指令（0其它 1新增 2修改 3删除 4登录 5退出登录）',
    `method`         varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '请求方法',
    `request_method` varchar(10) COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT '请求方式POST,GET,PUT,DELETE',
    `url`            varchar(266) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '请求URL',
    `param`          varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '请求参数',
    `result`         varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '返回参数',
    `ip`             varchar(45) COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT 'ip',
    `ip_area`        varchar(32) COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT '地址',
    `error_msg`      varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '错误消息',
    `created_at`     timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `a_m_o` (`admin_id`,`module`,`opt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `course_attachment` (
     `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
     `course_id` int(11) NOT NULL DEFAULT '0' COMMENT '课程ID',
     `sort` int(11) NOT NULL DEFAULT '0' COMMENT '升序',
     `title` varchar(255) NOT NULL DEFAULT '' COMMENT '附件名',
     `type` varchar(20) NOT NULL DEFAULT '' COMMENT '附件类型',
     `rid` int(11) NOT NULL DEFAULT '0' COMMENT '资源id',
     `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (`id`),
     KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;