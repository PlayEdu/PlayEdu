package xyz.playedu.api.service;

import xyz.playedu.api.domain.AppConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【app_config】的数据库操作Service
 * @createDate 2023-03-09 11:13:33
 */
public interface AppConfigService extends IService<AppConfig> {

    Map<String, Long> allKeys();

    List<AppConfig> allShow();

    void saveFromMap(HashMap<String, String> data);

    Map<String, String> keyValues();

}
