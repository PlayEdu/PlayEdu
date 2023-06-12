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
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.exception.NotFoundException;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service
 * @createDate 2023-03-15 10:16:45
 */
public interface CourseHourService extends IService<CourseHour> {

    CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void update(
            CourseHour courseHour, Integer chapterId, Integer sort, String title, Integer duration);

    List<CourseHour> getHoursByCourseId(Integer courseId);

    CourseHour create(
            Integer courseId,
            Integer chapterId,
            Integer sort,
            String title,
            String type,
            Integer rid,
            Integer duration);

    Integer getCountByCourseId(Integer courseId);

    Integer getCountByChapterId(Integer chapterId);

    void remove(Integer courseId, Integer chapterId);

    void updateSort(List<Integer> ids, Integer cid);

    List<Integer> getRidsByCourseId(Integer courseId, String type);

    List<CourseHour> chunk(List<Integer> hourIds);
}
