package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exams")
public class Exam {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Integer duration;
    private Integer passScore;
    private Integer totalScore;
    private Integer isShow;
    private Integer isRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Integer adminId;
}
