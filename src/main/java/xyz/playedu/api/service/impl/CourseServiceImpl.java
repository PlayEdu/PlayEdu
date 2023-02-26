package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.domain.CategoryCourse;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CategoryCourseService;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.mapper.CourseMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service实现
 * @createDate 2023-02-24 14:14:01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDepartmentService courseDepartmentService;

    @Autowired
    private CategoryCourseService categoryCourseService;

    @Override
    public PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter) {
        QueryWrapper<Course> wrapper = query().getWrapper().eq("1", "1");

        if (filter.getTitle() != null && filter.getTitle().length() > 0) {
            wrapper.like("title", "%" + filter.getTitle() + "%");
        }
        if (filter.getDepIds() != null && filter.getDepIds().length > 0) {
            List<Integer> courseIds = courseDepartmentService.getCourseIdsByDepIds(filter.getDepIds());
            if (courseIds.size() == 0) {
                wrapper.in("id", HelperUtil.zeroIntegerList());
            } else {
                wrapper.in("id", courseIds);
            }
        }
        if (filter.getCategoryIds() != null && filter.getCategoryIds().length > 0) {
            List<Integer> courseIds = categoryCourseService.getCourseIdsByCategoryIds(filter.getCategoryIds());
            if (courseIds.size() == 0) {
                wrapper.in("id", HelperUtil.zeroIntegerList());
            } else {
                wrapper.in("id", courseIds);
            }
        }

        if (filter.getSortAlgo().equals("desc")) {
            wrapper.orderByDesc(filter.getSortField());
        } else {
            wrapper.orderByAsc(filter.getSortField());
        }

        IPage<Course> pageObj = new Page<>(page, size);
        pageObj = page(pageObj, wrapper);

        PaginationResult<Course> pageResult = new PaginationResult<>();
        pageResult.setData(pageObj.getRecords());
        pageResult.setTotal(pageObj.getTotal());

        return pageResult;
    }

    @Override
    @Transactional
    public void createWithCategoryIdsAndDepIds(String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds) {
        Course course = new Course();
        course.setTitle(title);
        course.setThumb(thumb);
        course.setIsShow(isShow);
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());

        save(course);

        relateCategories(course, categoryIds);
        relateDepartments(course, depIds);
    }

    @Override
    public void relateDepartments(Course course, Integer[] depIds) {
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

    @Override
    public void resetRelateDepartments(Course course, Integer[] depIds) {
        courseDepartmentService.removeByCourseId(course.getId());
        relateDepartments(course, depIds);
    }

    @Override
    public void relateCategories(Course course, Integer[] categoryIds) {
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

    @Override
    public void resetRelateCategories(Course course, Integer[] categoryIds) {
        categoryCourseService.removeByCourseId(course.getId());
        relateCategories(course, categoryIds);
    }

    @Override
    @Transactional
    public void updateWithCategoryIdsAndDepIds(Course course, String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds) {
        Course newCourse = new Course();
        newCourse.setId(course.getId());

        if (!course.getTitle().equals(title)) {
            newCourse.setTitle(title);
        }
        if (!course.getThumb().equals(thumb)) {
            newCourse.setThumb(thumb);
        }
        if (!course.getIsShow().equals(isShow)) {
            newCourse.setIsShow(isShow);
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
        return courseDepartmentService.getDepIdsByCourseId(courseId);
    }

    @Override
    public List<Integer> getCategoryIdsByCourseId(Integer courseId) {
        return categoryCourseService.getCategoryIdsByCourseId(courseId);
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
        categoryCourseService.remove(categoryCourseService.query().getWrapper().eq("category_id", categoryId));
    }
}




