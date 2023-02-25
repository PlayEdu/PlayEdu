package xyz.playedu.api.service;

import xyz.playedu.api.domain.CategoryCourse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【category_course】的数据库操作Service
 * @createDate 2023-02-24 14:48:26
 */
public interface CategoryCourseService extends IService<CategoryCourse> {
    List<Integer> getCourseIdsByCategoryIds(Integer[] categoryIds);

    List<Integer> getCategoryIdsByCourseId(Integer id);

    void removeByCourseId(Integer courseId);
}
