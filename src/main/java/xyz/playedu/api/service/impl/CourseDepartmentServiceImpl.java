package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.mapper.CourseDepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_department】的数据库操作Service实现
 * @createDate 2023-02-24 14:53:52
 */
@Service
public class CourseDepartmentServiceImpl extends ServiceImpl<CourseDepartmentMapper, CourseDepartment>
        implements CourseDepartmentService {
    @Override
    public List<Integer> getCourseIdsByDepIds(List<Integer> depIds) {
        return list(query().getWrapper().in("dep_id", depIds)).stream().map(CourseDepartment::getCourseId).toList();
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream().map(CourseDepartment::getDepId).toList();
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }
}




