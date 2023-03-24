package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.UserLearnDurationStats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_stats】的数据库操作Mapper
 * @createDate 2023-03-22 13:55:29
 * @Entity xyz.playedu.api.domain.UserLearnDurationStats
 */
@Mapper
public interface UserLearnDurationStatsMapper extends BaseMapper<UserLearnDurationStats> {

    Integer getUserDateDuration(Integer userId, String createdDate);

    Integer getUserDuration(Integer userId);

}




