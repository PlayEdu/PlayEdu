package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.domain.ResourceCourseCategory;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.mapper.CourseMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.service.internal.ResourceCourseCategoryService;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ResourceCourseCategoryService courseCategoryService;

    @Override
    public PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter) {
        QueryWrapper<Course> wrapper = query().getWrapper().eq("1", "1");

        if (filter.getTitle() != null && filter.getTitle().length() > 0) {
            wrapper.like("title", "%" + filter.getTitle() + "%");
        }
        if (filter.getDepIds() != null && filter.getDepIds().trim().length() > 0) {
            List<Integer> depIds = Arrays.stream(filter.getDepIds().split(",")).map(Integer::valueOf).toList();
            List<Integer> courseIds = courseDepartmentService.getCourseIdsByDepIds(depIds);
            if (courseIds == null || courseIds.size() == 0) {
                courseIds = HelperUtil.zeroIntegerList();
            }
            wrapper.in("id", courseIds);
        }
        if (filter.getCategoryIds() != null && filter.getCategoryIds().trim().length() > 0) {
            List<Integer> categoryIds = Arrays.stream(filter.getCategoryIds().split(",")).map(Integer::valueOf).toList();
            List<Integer> courseIds = courseCategoryService.getCourseIdsByCategoryIds(categoryIds);
            if (courseIds == null || courseIds.size() == 0) {
                courseIds = HelperUtil.zeroIntegerList();
            }
            wrapper.in("id", courseIds);
        }
        if (filter.getIsShow() != null) {
            wrapper.eq("is_show", filter.getIsShow());
        }

        String sortFiled = filter.getSortField();
        if (sortFiled == null || sortFiled.trim().length() == 0) {
            sortFiled = "id";
        }
        String sortAlgo = filter.getSortAlgo();
        if (sortAlgo == null || sortAlgo.trim().length() == 0) {
            sortAlgo = "desc";
        }
        if ("desc".equals(sortAlgo)) {
            wrapper.orderByDesc(sortFiled);
        } else {
            wrapper.orderByAsc(sortFiled);
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
    public Course createWithCategoryIdsAndDepIds(String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds) {
        // 创建课程
        Course course = new Course();
        course.setTitle(title);
        course.setThumb(thumb);
        course.setIsShow(isShow);
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
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
        List<CourseDepartment> courseDepartments = new ArrayList<>();
        for (int i = 0; i < depIds.length; i++) {
            Integer tmpDepId = depIds[i];
            courseDepartments.add(new CourseDepartment() {{
                setCourseId(course.getId());
                setDepId(tmpDepId);
            }});
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
        List<ResourceCourseCategory> resourceCourseCategories = new ArrayList<>();
        for (int i = 0; i < categoryIds.length; i++) {
            Integer tmpCategoryId = categoryIds[i];
            resourceCourseCategories.add(new ResourceCourseCategory() {{
                setCategoryId(tmpCategoryId);
                setCourseId(course.getId());
            }});
        }
        courseCategoryService.saveBatch(resourceCourseCategories);
    }

    @Override
    public void resetRelateCategories(Course course, Integer[] categoryIds) {
        courseCategoryService.removeByCourseId(course.getId());
        relateCategories(course, categoryIds);
    }

    @Override
    @Transactional
    public void updateWithCategoryIdsAndDepIds(Course course, String title, String thumb, Integer isShow, Integer[] categoryIds, Integer[] depIds) {
        Course newCourse = new Course();
        newCourse.setId(course.getId());
        newCourse.setTitle(title);
        newCourse.setThumb(thumb);
        newCourse.setIsShow(isShow);

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
        return list(query().getWrapper().in("id", ids));
    }
}




