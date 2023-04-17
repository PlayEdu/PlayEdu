/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserCourseRecordPaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:04
 */
public interface UserCourseRecordService extends IService<UserCourseRecord> {

    UserCourseRecord find(Integer userId, Integer courseId);

    void storeOrUpdate(Integer userId, Integer courseId, Integer hourCount, Integer finishedCount);

    List<UserCourseRecord> chunk(Integer userId, List<Integer> courseIds);

    PaginationResult<UserCourseRecord> paginate(
            int page, int size, UserCourseRecordPaginateFilter filter);

    void destroy(Integer courseId, List<Integer> ids);

    void removeByCourseId(Integer courseId);

    List<UserCourseRecord> chunks(List<Integer> ids, List<String> fields);
}
