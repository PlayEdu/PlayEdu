/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.ResourceCourseCategory;
import xyz.playedu.api.mapper.ResourceCourseCategoryMapper;
import xyz.playedu.api.service.internal.ResourceCourseCategoryService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_course_category】的数据库操作Service实现
 * @createDate 2023-03-09 09:54:22
 */
@Service
public class ResourceCourseCategoryServiceImpl
        extends ServiceImpl<ResourceCourseCategoryMapper, ResourceCourseCategory>
        implements ResourceCourseCategoryService {

    @Override
    public List<Integer> getCourseIdsByCategoryIds(List<Integer> categoryIds) {
        return list(query().getWrapper().in("category_id", categoryIds)).stream()
                .map(ResourceCourseCategory::getCourseId)
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
                .map(ResourceCourseCategory::getCategoryId)
                .toList();
    }
}
