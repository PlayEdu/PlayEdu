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

import xyz.playedu.api.domain.Course;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service
 * @createDate 2023-02-24 14:14:01
 */
public interface CourseService extends IService<Course> {

    PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter);

    Course createWithCategoryIdsAndDepIds(
            String title,
            String thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            Integer[] categoryIds,
            Integer[] depIds);

    void updateWithCategoryIdsAndDepIds(
            Course course,
            String title,
            String thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            Integer[] categoryIds,
            Integer[] depIds);

    void relateDepartments(Course course, Integer[] depIds);

    void resetRelateDepartments(Course course, Integer[] depIds);

    void relateCategories(Course course, Integer[] categoryIds);

    void resetRelateCategories(Course course, Integer[] categoryIds);

    Course findOrFail(Integer id) throws NotFoundException;

    List<Integer> getDepIdsByCourseId(Integer courseId);

    List<Integer> getCategoryIdsByCourseId(Integer courseId);

    void updateClassHour(Integer courseId, Integer classHour);

    void removeCategoryIdRelate(Integer categoryId);

    List<Course> chunks(List<Integer> ids, List<String> fields);

    List<Course> chunks(List<Integer> ids);

    List<Course> getOpenCoursesAndShow(Integer limit);

    List<Course> getDepCoursesAndShow(List<Integer> depIds);

    Map<Integer, List<Integer>> getCategoryIdsGroup(List<Integer> courseIds);

    Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> courseIds);

    Long total();
}
