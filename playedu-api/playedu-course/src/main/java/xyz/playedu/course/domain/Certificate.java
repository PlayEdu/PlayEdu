package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("certificates")
public class Certificate {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer examId;
    private Integer score;
    private LocalDateTime issueDate;
    private LocalDateTime expireDate;
    private String certificateNo;
    private LocalDateTime createdAt;
}
