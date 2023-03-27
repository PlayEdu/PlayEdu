package xyz.playedu.api.types.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.UserCourseRecord;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/27 15:29
 */
@Data
public class UserLatestLearn {
    @JsonProperty("course")
    private Course course;
    @JsonProperty("record")
    private UserCourseRecord userCourseRecord;
}
