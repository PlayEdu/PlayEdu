package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminLog;
import xyz.playedu.api.service.AdminLogService;
import xyz.playedu.api.mapper.AdminLogMapper;
import org.springframework.stereotype.Service;

/**
* @author tengteng
* @description 针对表【admin_logs】的数据库操作Service实现
* @createDate 2023-02-17 15:40:31
*/
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
    implements AdminLogService{

}




