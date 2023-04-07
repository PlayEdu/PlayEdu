/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.internal;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.ResourceCourseCategory;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_course_category】的数据库操作Service
 * @createDate 2023-03-09 09:54:22
 */
public interface ResourceCourseCategoryService extends IService<ResourceCourseCategory> {

    List<Integer> getCourseIdsByCategoryIds(List<Integer> categoryIds);

    void removeByCourseId(Integer id);

    void removeByCategoryId(Integer id);

    List<Integer> getCategoryIdsByCourseId(Integer courseId);
}
