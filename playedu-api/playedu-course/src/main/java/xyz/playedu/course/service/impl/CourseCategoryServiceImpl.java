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
package xyz.playedu.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.playedu.course.domain.CourseCategory;
import xyz.playedu.course.mapper.CourseCategoryMapper;
import xyz.playedu.course.service.CourseCategoryService;

/**
 * @author tengteng
 * @description 针对表【resource_course_category】的数据库操作Service实现
 * @createDate 2023-03-09 09:54:22
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {

    @Override
    public List<Integer> getCourseIdsByCategoryIds(List<Integer> categoryIds) {
        return list(query().getWrapper().in("category_id", categoryIds)).stream()
                .map(CourseCategory::getCourseId)
                .toList();
    }

    @Override
    public void removeByCourseId(Integer id) {
        remove(query().getWrapper().eq("course_id", id));
    }

    @Override
    public void removeByCategoryId(Integer id) {
        remove(query().getWrapper().eq("category_id", id));
    }

    @Override
    public List<Integer> getCategoryIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseCategory::getCategoryId)
                .toList();
    }

    @Override
    public List<Integer> getCourseIdsByCategoryId(Integer id) {
        return list(query().getWrapper().eq("category_id", id)).stream()
                .map(CourseCategory::getCourseId)
                .toList();
    }
}
