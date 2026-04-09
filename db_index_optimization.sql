-- 为courses表添加索引
ALTER TABLE `courses` ADD INDEX `idx_is_show` (`is_show`);
ALTER TABLE `courses` ADD INDEX `idx_is_required` (`is_required`);
ALTER TABLE `courses` ADD INDEX `idx_admin_id` (`admin_id`);
ALTER TABLE `courses` ADD INDEX `idx_created_at` (`created_at`);
ALTER TABLE `courses` ADD INDEX `idx_sort_at` (`sort_at`);

-- 为user_course_records表添加索引
ALTER TABLE `user_course_records` ADD INDEX `idx_user_id` (`user_id`);
ALTER TABLE `user_course_records` ADD INDEX `idx_course_id` (`course_id`);
ALTER TABLE `user_course_records` ADD INDEX `idx_is_finished` (`is_finished`);
ALTER TABLE `user_course_records` ADD INDEX `idx_progress` (`progress`);
ALTER TABLE `user_course_records` ADD INDEX `idx_user_course` (`user_id`, `course_id`);

-- 为course_department_user表添加索引
ALTER TABLE `course_department_user` ADD INDEX `idx_course_id` (`course_id`);
ALTER TABLE `course_department_user` ADD INDEX `idx_range_id` (`range_id`);

-- 为resource_course_category表添加索引
ALTER TABLE `resource_course_category` ADD INDEX `idx_course_id` (`course_id`);
ALTER TABLE `resource_course_category` ADD INDEX `idx_category_id` (`category_id`);

-- 为users表添加索引
ALTER TABLE `users` ADD INDEX `idx_email` (`email`);
ALTER TABLE `users` ADD INDEX `idx_name` (`name`);
ALTER TABLE `users` ADD INDEX `idx_id_card` (`id_card`);

-- 为user_course_hour_records表添加索引
ALTER TABLE `user_course_hour_records` ADD INDEX `idx_user_id` (`user_id`);
ALTER TABLE `user_course_hour_records` ADD INDEX `idx_course_id` (`course_id`);
ALTER TABLE `user_course_hour_records` ADD INDEX `idx_hour_id` (`hour_id`);
ALTER TABLE `user_course_hour_records` ADD INDEX `idx_is_finished` (`is_finished`);
