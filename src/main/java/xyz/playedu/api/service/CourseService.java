package xyz.playedu.api.service;

import xyz.playedu.api.domain.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service
 * @createDate 2023-02-24 14:14:01
 */
public interface CourseService extends IService<Course> {

    PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter);

    void createWithCategoryIdsAndDepIds(String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds);

    void updateWithCategoryIdsAndDepIds(Course course, String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds);

    void relateDepartments(Course course, Integer[] depIds);

    void resetRelateDepartments(Course course, Integer[] depIds);

    void relateCategories(Course course, Integer[] categoryIds);

    void resetRelateCategories(Course course, Integer[] categoryIds);

    Course findOrFail(Integer id) throws NotFoundException;

    List<Integer> getDepIdsByCourseId(Integer courseId);

    List<Integer> getCategoryIdsByCourseId(Integer courseId);
}
