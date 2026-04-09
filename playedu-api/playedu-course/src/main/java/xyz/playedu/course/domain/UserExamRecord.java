package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_exam_records")
public class UserExamRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer examId;
    private Integer score;
    private Integer isPass;
    private Integer usedTime;
    private String answers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
