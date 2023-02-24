package xyz.playedu.api.service;

import xyz.playedu.api.domain.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

/**
* @author tengteng
* @description 针对表【courses】的数据库操作Service
* @createDate 2023-02-24 14:14:01
*/
public interface CourseService extends IService<Course> {

    PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter);

}
