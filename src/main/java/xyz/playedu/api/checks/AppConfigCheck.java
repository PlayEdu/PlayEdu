package xyz.playedu.api.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.service.AppConfigService;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/9 13:29
 */
@Component
public class AppConfigCheck implements ApplicationRunner {

    private static final HashMap<String, AppConfig[]> configs = new HashMap<>() {{
        // 系统配置
        put("系统", new AppConfig[]{new AppConfig() {{
            setName("网站名");
            setSort(10);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
            setKeyName("system.name");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("Logo");
            setSort(20);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
            setKeyName("system.logo");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("API访问地址");
            setSort(30);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
            setKeyName("system.api_url");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("PC端口访问地址");
            setSort(40);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
            setKeyName("system.pc_url");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("H5端口访问地址");
            setSort(50);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
            setKeyName("system.h5_url");
            setKeyValue("");
        }},});
        // 播放器配置
        put("播放器配置", new AppConfig[]{new AppConfig() {{
            setName("播放器封面");
            setSort(10);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
            setKeyName("player.poster");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("启用跑马灯");
            setSort(20);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_SWITCH);
            setKeyName("player.is_enabled_bullet_secret");
            setKeyValue("0");
        }}, new AppConfig() {{
            setName("跑马灯内容");
            setSort(30);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
            setKeyName("player.bullet_secret_text");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("跑马灯颜色");
            setSort(40);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
            setKeyName("player.bullet_secret_color");
            setKeyValue("");
        }}, new AppConfig() {{
            setName("跑马灯透明度");
            setSort(50);
            setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
            setKeyName("player.bullet_secret_opacity");
            setKeyValue("1");
        }},});
    }};

    @Autowired
    private AppConfigService configService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Long> keys = configService.allKeys();
        List<AppConfig> list = new ArrayList<>();
        Date now = new Date();

        configs.forEach((groupNameValue, items) -> {
            for (int i = 0; i < items.length; i++) {
                AppConfig configItem = items[i];

                if (keys.get(configItem.getKeyName()) != null) {
                    continue;
                }

                configItem.setGroupName(groupNameValue);
                configItem.setCreatedAt(now);
                list.add(configItem);
            }
        });

        if (list.size() > 0) {
            configService.saveBatch(list);
        }
    }

}
