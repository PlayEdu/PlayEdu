package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.CategoryCourse;
import xyz.playedu.api.service.CategoryCourseService;
import xyz.playedu.api.mapper.CategoryCourseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【category_course】的数据库操作Service实现
 * @createDate 2023-02-24 14:48:26
 */
@Service
public class CategoryCourseServiceImpl extends ServiceImpl<CategoryCourseMapper, CategoryCourse>
        implements CategoryCourseService {
    @Override
    public List<Integer> getCourseIdsByCategoryIds(Integer[] categoryIds) {
        List<Integer> ids = new ArrayList<>();
        List<CategoryCourse> categoryCourses = list(query().getWrapper().in("category_id", categoryIds));
        if (categoryCourses.size() == 0) {
            return ids;
        }
        for (CategoryCourse categoryCourse : categoryCourses) {
            ids.add(categoryCourse.getCourseId());
        }
        return ids;
    }

    @Override
    public List<Integer> getCategoryIdsByCourseId(Integer id) {
        List<Integer> ids = new ArrayList<>();
        List<CategoryCourse> categoryCourses = list(query().getWrapper().eq("course_id", id));
        if (categoryCourses.size() == 0) {
            return ids;
        }
        for (CategoryCourse categoryCourse : categoryCourses) {
            ids.add(categoryCourse.getCategoryId());
        }
        return ids;
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }
}




