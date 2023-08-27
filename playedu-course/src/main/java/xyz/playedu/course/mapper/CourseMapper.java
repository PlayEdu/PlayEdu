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
package xyz.playedu.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.common.types.paginate.CoursePaginateFiler;
import xyz.playedu.course.domain.Course;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Mapper
 * @createDate 2023-03-20 14:25:31
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    List<Course> paginate(CoursePaginateFiler filer);

    Long paginateCount(CoursePaginateFiler filer);

    List<Course> openCoursesAndShow(Integer limit, Integer categoryId);
}
