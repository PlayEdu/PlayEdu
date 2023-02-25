package xyz.playedu.api.service;

import xyz.playedu.api.domain.CourseCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_categories】的数据库操作Service
 * @createDate 2023-02-24 13:55:19
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    List<CourseCategory> listByParentId(Integer id);

    List<CourseCategory> all();

    CourseCategory findOrFail(Integer id) throws NotFoundException;

    void deleteById(Integer id) throws NotFoundException;

    void update(CourseCategory category, String name, Integer parentId, Integer sort) throws NotFoundException;

    void create(String name, Integer parentId, Integer sort) throws NotFoundException;

}
