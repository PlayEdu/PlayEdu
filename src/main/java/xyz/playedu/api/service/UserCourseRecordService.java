package xyz.playedu.api.service;

import xyz.playedu.api.domain.UserCourseRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.CourseUserPaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:04
 */
public interface UserCourseRecordService extends IService<UserCourseRecord> {

    UserCourseRecord find(Integer userId, Integer courseId);

    void storeOrUpdate(Integer userId, Integer courseId, Integer hourCount, Integer finishedCount);

    List<UserCourseRecord> chunk(Integer userId, List<Integer> courseIds);

    PaginationResult<UserCourseRecord> paginate(int page, int size, CourseUserPaginateFilter filter);

    void destroy(Integer courseId, List<Integer> ids);

    void removeByCourseId(Integer courseId);

    List<UserCourseRecord> chunks(List<Integer> ids, List<String> fields);
}
