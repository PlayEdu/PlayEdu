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
    public List<Integer> getCourseIdsByDepIds(Integer[] depIds) {
        List<Integer> ids = new ArrayList<>();
        List<CourseDepartment> courseDepartments = list(query().getWrapper().in("dep_id", depIds));
        if (courseDepartments.size() == 0) {
            return ids;
        }
        for (CourseDepartment courseDepartment : courseDepartments) {
            ids.add(courseDepartment.getCourseId());
        }
        return ids;
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        List<Integer> ids = new ArrayList<>();
        List<CourseDepartment> courseDepartments = list(query().getWrapper().eq("course_id", courseId));
        if (courseDepartments.size() == 0) {
            return ids;
        }
        for (CourseDepartment courseDepartment : courseDepartments) {
            ids.add(courseDepartment.getDepId());
        }
        return ids;
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }
}




