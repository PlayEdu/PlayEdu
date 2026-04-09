package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_paths")
public class LearningPath {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Integer isShow;
    private Integer isRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Integer adminId;
}
