package xyz.playedu.common.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    /**
     * 设置缓存（默认30分钟）
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        set(key, value, 30, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 删除缓存
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     * @param key 键
     * @param unit 时间单位
     * @return 过期时间
     */
    public long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }
}
