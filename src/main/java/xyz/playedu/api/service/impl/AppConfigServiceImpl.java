package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.mapper.AppConfigMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【app_config】的数据库操作Service实现
 * @createDate 2023-03-09 11:13:33
 */
@Service
public class AppConfigServiceImpl extends ServiceImpl<AppConfigMapper, AppConfig>
        implements AppConfigService {

    @Override
    public Map<String, Long> allKeys() {
        return list().stream().collect(Collectors.toMap(AppConfig::getKeyName, AppConfig::getId));
    }

    @Override
    public List<AppConfig> allShow() {
        return list(query().getWrapper().eq("is_hidden", 0));
    }

    @Override
    public void saveFromMap(HashMap<String, String> data) {
        Map<String, AppConfig> configs = list(query().getWrapper().in("key_name", data.keySet())).stream().collect(Collectors.toMap(AppConfig::getKeyName, e -> e));
        List<AppConfig> list = new ArrayList<>();

        data.forEach((keyNameValue, keyValueValue) -> {
            if ("******".equals(keyNameValue)) {//私密信息默认place
                return;
            }
            AppConfig configItem = configs.get(keyNameValue);
            if (configItem == null) {//不存在的配置
                return;
            }
            if (keyValueValue.equals(configItem.getKeyValue())) {//没有变化
                return;
            }
            list.add(new AppConfig() {{
                setId(configItem.getId());
                setKeyValue(keyValueValue);
            }});
        });

        if (list.size() > 0) {
            updateBatchById(list);
        }
    }

    @Override
    public Map<String, String> keyValues() {
        return list(query().getWrapper().eq("is_hidden", 0)).stream().collect(Collectors.toMap(AppConfig::getKeyName, AppConfig::getKeyValue));
    }
}




