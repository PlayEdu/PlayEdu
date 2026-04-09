package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_plan_tasks")
public class LearningPlanTask {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer planId;
    private Integer type;
    private Integer targetId;
    private LocalDateTime deadline;
    private Integer isCompleted;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
