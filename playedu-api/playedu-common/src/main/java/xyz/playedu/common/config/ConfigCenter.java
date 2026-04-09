package xyz.playedu.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.service.AppConfigService;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConfigCenter {

    @Autowired
    private AppConfigService appConfigService;

    private static final Map<String, String> configMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        reloadConfig();
    }

    /**
     * 重新加载配置
     */
    public void reloadConfig() {
        Map<String, String> configs = appConfigService.keyValues();
        configMap.clear();
        configMap.putAll(configs);
    }

    /**
     * 获取配置值
     * @param key 配置键
     * @return 配置值
     */
    public String get(String key) {
        return configMap.get(key);
    }

    /**
     * 获取配置值，带默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String get(String key, String defaultValue) {
        String value = configMap.get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取整数类型配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 整数配置值
     */
    public int getInt(String key, int defaultValue) {
        String value = configMap.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取布尔类型配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 布尔配置值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = configMap.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * 获取所有配置
     * @return 所有配置
     */
    public Map<String, String> getAll() {
        return new ConcurrentHashMap<>(configMap);
    }
}
