package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_learning_path_records")
public class UserLearningPathRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer pathId;
    private Integer completedNodes;
    private Integer totalNodes;
    private Integer progress;
    private Integer isFinished;
    private LocalDateTime finishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
