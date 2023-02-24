package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.service.CategoryCourseService;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.mapper.CourseMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.util.HelperUtil;

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
}




