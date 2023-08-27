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

import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.course.domain.CourseChapter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Service
 * @createDate 2023-02-26 17:30:19
 */
public interface CourseChapterService extends IService<CourseChapter> {

    List<CourseChapter> getChaptersByCourseId(Integer courseId);

    void create(Integer courseId, String name, Integer sort);

    void update(CourseChapter chapter, String name, Integer sort);

    CourseChapter findOrFail(Integer id) throws NotFoundException;

    CourseChapter findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void updateSort(List<Integer> ids, Integer cid);
}
