package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam_questions")
public class ExamQuestion {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer examId;
    private Integer type;
    private String content;
    private String options;
    private String answer;
    private Integer score;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
