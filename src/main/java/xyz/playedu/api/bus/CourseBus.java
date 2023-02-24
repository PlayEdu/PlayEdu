package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.CategoryCourse;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.service.CategoryCourseService;
import xyz.playedu.api.service.CourseDepartmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 17:12
 */
@Component
public class CourseBus {

    @Autowired
    private CourseDepartmentService courseDepartmentService;

    @Autowired
    private CategoryCourseService categoryCourseService;

    public void departmentRelate(Course course, Integer[] depIds) {
        if (depIds == null || depIds.length == 0) {
            return;
        }
        List<CourseDepartment> courseDepartments = new ArrayList<>();
        for (int i = 0; i < depIds.length; i++) {
            CourseDepartment courseDepartment = new CourseDepartment();
            courseDepartment.setCourseId(course.getId());
            courseDepartment.setDepId(depIds[i]);
            courseDepartments.add(courseDepartment);
        }
        courseDepartmentService.saveBatch(courseDepartments);
    }

    public void resetDepartmentRelate(Course course, Integer[] depIds) {
        courseDepartmentService.removeByCourseId(course.getId());
        departmentRelate(course, depIds);
    }

    public void categoryRelate(Course course, Integer[] categoryIds) {
        if (categoryIds == null || categoryIds.length == 0) {
            return;
        }
        List<CategoryCourse> categoryCourses = new ArrayList<>();
        for (int i = 0; i < categoryIds.length; i++) {
            CategoryCourse categoryCourse = new CategoryCourse();
            categoryCourse.setCourseId(course.getId());
            categoryCourse.setCategoryId(categoryIds[i]);
            categoryCourses.add(categoryCourse);
        }
        categoryCourseService.saveBatch(categoryCourses);
    }

    public void resetCategoryRelate(Course course, Integer[] categoryIds) {
        categoryCourseService.removeByCourseId(course.getId());
        categoryRelate(course, categoryIds);
    }

}
