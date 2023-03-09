package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.mapper.AppConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author tengteng
* @description 针对表【app_config】的数据库操作Service实现
* @createDate 2023-03-09 11:13:33
*/
@Service
public class AppConfigServiceImpl extends ServiceImpl<AppConfigMapper, AppConfig>
    implements AppConfigService{

}




