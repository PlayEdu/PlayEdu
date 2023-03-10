package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 15:27
 */
@Getter
@Setter
public class DepartmentDestroyEvent extends ApplicationEvent {

    private Integer depId;
    private Integer adminId;
    private Date at;

    public DepartmentDestroyEvent(Object source, Integer adminId, Integer depId, Date date) {
        super(source);
        this.adminId = adminId;
        this.depId = depId;
        this.at = date;
    }
}
