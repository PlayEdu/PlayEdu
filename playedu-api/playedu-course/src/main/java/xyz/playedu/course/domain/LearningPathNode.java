package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_path_nodes")
public class LearningPathNode {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pathId;
    private Integer type;
    private Integer targetId;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
