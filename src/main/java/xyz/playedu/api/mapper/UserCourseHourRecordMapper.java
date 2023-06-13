/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.UserCourseHourRecord;
import xyz.playedu.api.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.playedu.api.types.mapper.UserCourseHourRecordUserCountMapper;
import xyz.playedu.api.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.playedu.api.types.paginate.UserCourseHourRecordPaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Mapper
 * @createDate 2023-03-20 16:41:08 @Entity xyz.playedu.api.domain.UserCourseHourRecord
 */
@Mapper
public interface UserCourseHourRecordMapper extends BaseMapper<UserCourseHourRecord> {
    List<UserCourseHourRecord> getUserLatestRecords(Integer userId, Integer size);

    List<UserCourseHourRecordCourseCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished);

    List<UserCourseHourRecordUserCountMapper> getUserCourseHourUserCount(
            Integer courseId, List<Integer> userIds, Integer isFinished);

    List<UserCourseHourRecordUserFirstCreatedAtMapper> getUserCourseHourUserFirstCreatedAt(
            Integer courseId, List<Integer> userIds);

    List<UserCourseHourRecord> paginate(UserCourseHourRecordPaginateFilter filter);

    Long paginateCount(UserCourseHourRecordPaginateFilter filter);
}
