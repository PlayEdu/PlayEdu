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
package xyz.playedu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.common.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.playedu.common.types.mapper.UserCourseHourRecordUserCountMapper;
import xyz.playedu.common.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.types.paginate.UserCourseHourRecordPaginateFilter;
import xyz.playedu.course.domain.UserCourseHourRecord;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:08
 */
public interface UserCourseHourRecordService extends IService<UserCourseHourRecord> {
    UserCourseHourRecord find(Integer userId, Integer courseId, Integer hourId);

    boolean storeOrUpdate(
            Integer userId,
            Integer courseId,
            Integer hourId,
            Integer duration,
            Integer totalDuration);

    Integer getFinishedHourCount(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getRecords(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getLatestCourseIds(Integer userId, Integer size);

    void removeByCourseId(Integer courseId);

    void remove(Integer userId, Integer courseId);

    void remove(Integer userId);

    void remove(Integer userId, Integer courseId, Integer hourId);

    List<UserCourseHourRecordCourseCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished);

    List<UserCourseHourRecordUserCountMapper> getUserCourseHourUserCount(
            Integer courseId, List<Integer> userIds, Integer isFinished);

    List<UserCourseHourRecordUserFirstCreatedAtMapper> getUserCourseHourUserFirstCreatedAt(
            Integer courseId, List<Integer> userIds);

    PaginationResult<UserCourseHourRecord> paginate(
            int page, int size, UserCourseHourRecordPaginateFilter filter);
}
