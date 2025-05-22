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

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.types.paginate.CoursePaginateFiler;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.course.domain.Course;
import xyz.playedu.course.domain.CourseCategory;
import xyz.playedu.course.domain.CourseDepartmentUser;
import xyz.playedu.course.mapper.CourseMapper;
import xyz.playedu.course.service.CourseCategoryService;
import xyz.playedu.course.service.CourseDepartmentUserService;
import xyz.playedu.course.service.CourseService;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service实现
 * @createDate 2023-02-24 14:14:01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired private CourseDepartmentUserService courseDepartmentUserService;

    @Autowired private CourseCategoryService courseCategoryService;

    @Override
    public PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter) {
        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        PaginationResult<Course> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    @Transactional
    public Course createWithCategoryIdsAndDepIds(
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            Integer[] categoryIds,
            Integer[] depIds,
            Integer adminId) {
        // 创建课程
        Course course = new Course();
        course.setTitle(title);
        course.setThumb(thumb);
        course.setShortDesc(shortDesc);
        course.setIsShow(isShow);
        course.setIsRequired(isRequired);
        course.setSortAt(new Date());
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        course.setAdminId(adminId);
        save(course);
        // 关联分类
        relateCategories(course, categoryIds);
        // 关联部门
        relateDepartments(course, depIds);

        return course;
    }

    @Override
    public void relateDepartments(Course course, Integer[] depIds) {
        if (depIds == null || depIds.length == 0) {
            return;
        }
        List<CourseDepartmentUser> courseDepartmentUsers = new ArrayList<>();
        for (int i = 0; i < depIds.length; i++) {
            Integer tmpDepId = depIds[i];
            courseDepartmentUsers.add(
                    new CourseDepartmentUser() {
                        {
                            setCourseId(course.getId());
                            setRangeId(tmpDepId);
                        }
                    });
        }
        courseDepartmentUserService.saveBatch(courseDepartmentUsers);
    }

    @Override
    public void resetRelateDepartments(Course course, Integer[] depIds) {
        courseDepartmentUserService.removeByCourseId(course.getId());
        relateDepartments(course, depIds);
    }

    @Override
    public void relateCategories(Course course, Integer[] categoryIds) {
        if (categoryIds == null || categoryIds.length == 0) {
            return;
        }
        List<CourseCategory> courseCategories = new ArrayList<>();
        for (int i = 0; i < categoryIds.length; i++) {
            Integer tmpCategoryId = categoryIds[i];
            courseCategories.add(
                    new CourseCategory() {
                        {
                            setCategoryId(tmpCategoryId);
                            setCourseId(course.getId());
                        }
                    });
        }
        courseCategoryService.saveBatch(courseCategories);
    }

    @Override
    public void resetRelateCategories(Course course, Integer[] categoryIds) {
        courseCategoryService.removeByCourseId(course.getId());
        relateCategories(course, categoryIds);
    }

    @Override
    @Transactional
    public void updateWithCategoryIdsAndDepIds(
            Course course,
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            String sortAt,
            Integer[] categoryIds,
            Integer[] depIds) {
        Course newCourse = new Course();
        newCourse.setId(course.getId());
        newCourse.setTitle(title);
        newCourse.setThumb(thumb);
        newCourse.setIsShow(isShow);
        newCourse.setIsRequired(isRequired);
        newCourse.setShortDesc(shortDesc);

        if (StringUtil.isNotEmpty(sortAt)) {
            newCourse.setSortAt(DateUtil.parseDate(sortAt));
        }

        updateById(newCourse);

        resetRelateCategories(newCourse, categoryIds);
        resetRelateDepartments(newCourse, depIds);
    }

    @Override
    public Course findOrFail(Integer id) throws NotFoundException {
        Course course = getOne(query().getWrapper().eq("id", id));
        if (course == null) {
            throw new NotFoundException("课程不存在");
        }
        return course;
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return courseDepartmentUserService.getDepIdsByCourseId(courseId);
    }

    @Override
    public List<Integer> getCategoryIdsByCourseId(Integer courseId) {
        return courseCategoryService.getCategoryIdsByCourseId(courseId);
    }

    @Override
    public void updateClassHour(Integer courseId, Integer classHour) {
        Course course = new Course();
        course.setId(courseId);
        course.setClassHour(classHour);
        updateById(course);
    }

    @Override
    public void removeCategoryIdRelate(Integer categoryId) {
        courseCategoryService.removeByCategoryId(categoryId);
    }

    @Override
    public List<Course> chunks(List<Integer> ids, List<String> fields) {
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public List<Course> chunks(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public List<Course> getOpenCoursesAndShow(Integer limit) {
        return getBaseMapper().openCoursesAndShow(limit, new ArrayList<>());
    }

    @Override
    public List<Course> getOpenCoursesAndShow(Integer limit, List<Integer> categoryIds) {
        return getBaseMapper().openCoursesAndShow(limit, categoryIds);
    }

    @SneakyThrows
    @Override
    public List<Course> getDepCoursesAndShow(List<Integer> depIds, List<Integer> categoryIds) {
        if (StringUtil.isEmpty(depIds)) {
            return new ArrayList<>();
        }
        // 获取部门课程ID
        List<Integer> courseIds = courseDepartmentUserService.getCourseIdsByDepIds(depIds);
        if (StringUtil.isEmpty(courseIds)) {
            return new ArrayList<>();
        }

        if (StringUtil.isNotEmpty(categoryIds)) {
            // 获取分类课程ID
            List<Integer> catCourseIds =
                    courseCategoryService.getCourseIdsByCategoryIds(categoryIds);
            if (StringUtil.isEmpty(catCourseIds)) {
                return new ArrayList<>();
            }
            // 求课程ID交集
            courseIds = courseIds.stream().filter(catCourseIds::contains).toList();
            if (StringUtil.isEmpty(courseIds)) {
                return new ArrayList<>();
            }
        }
        return list(query().getWrapper().in("id", courseIds).eq("is_show", 1));
    }

    @Override
    public List<Course> getDepCoursesAndShow(List<Integer> depIds) {
        if (StringUtil.isEmpty(depIds)) {
            return new ArrayList<>();
        }
        // 获取部门课程ID
        List<Integer> courseIds = courseDepartmentUserService.getCourseIdsByDepIds(depIds);
        if (StringUtil.isEmpty(courseIds)) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", courseIds).eq("is_show", 1));
    }

    @Override
    public Map<Integer, List<Integer>> getCategoryIdsGroup(List<Integer> courseIds) {
        if (courseIds == null || courseIds.size() == 0) {
            return null;
        }
        Map<Integer, List<CourseCategory>> data =
                courseCategoryService
                        .list(courseCategoryService.query().getWrapper().in("course_id", courseIds))
                        .stream()
                        .collect(Collectors.groupingBy(CourseCategory::getCourseId));
        Map<Integer, List<Integer>> result = new HashMap<>();
        data.forEach(
                (courseId, records) -> {
                    result.put(
                            courseId, records.stream().map(CourseCategory::getCategoryId).toList());
                });
        return result;
    }

    @Override
    public Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> courseIds) {
        if (courseIds == null || courseIds.size() == 0) {
            return null;
        }
        Map<Integer, List<CourseDepartmentUser>> data =
                courseDepartmentUserService
                        .list(
                                courseDepartmentUserService
                                        .query()
                                        .getWrapper()
                                        .in("course_id", courseIds))
                        .stream()
                        .collect(Collectors.groupingBy(CourseDepartmentUser::getCourseId));
        Map<Integer, List<Integer>> result = new HashMap<>();
        data.forEach(
                (courseId, records) -> {
                    result.put(
                            courseId,
                            records.stream().map(CourseDepartmentUser::getRangeId).toList());
                });
        return result;
    }

    @Override
    public Long total() {
        return count();
    }
}
