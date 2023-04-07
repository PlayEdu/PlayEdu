/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.UserCourseHourRecord;
import xyz.playedu.api.types.mapper.UserCourseHourRecordCountMapper;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Mapper
 * @createDate 2023-03-20 16:41:08 @Entity xyz.playedu.api.domain.UserCourseHourRecord
 */
@Mapper
public interface UserCourseHourRecordMapper extends BaseMapper<UserCourseHourRecord> {
    List<UserCourseHourRecord> getUserLatestRecords(Integer userId, Integer size);

    List<UserCourseHourRecordCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished);
}
