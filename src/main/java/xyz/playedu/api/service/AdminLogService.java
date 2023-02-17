package xyz.playedu.api.service;

import org.springframework.stereotype.Service;
import xyz.playedu.api.domain.AdminLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Service
 * @createDate 2023-02-17 15:40:31
 */
@Service
public interface AdminLogService extends IService<AdminLog> {

}
