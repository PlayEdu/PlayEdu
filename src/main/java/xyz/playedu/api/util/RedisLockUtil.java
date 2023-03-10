package xyz.playedu.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/10 14:48
 */
@Slf4j
public class RedisLockUtil {

    public final static String LUA_LOCK_CREATE = """
             if redis.call("GET", KEYS[1]) == ARGV[1] then
             	redis.call("SET", KEYS[1], ARGV[1], "PX", ARGV[2])
             	return "OK"
             else
             	return redis.call("SET", KEYS[1], ARGV[1], "NX", "PX", ARGV[2])
             end
            """;

    public final static String LUA_LOCK_REMOVE = """
            if redis.call("GET", KEYS[1]) == ARGV[1] then
            	return redis.call("DEL", KEYS[1])
            else
            	return 0
            end""";

    public static boolean lock(String key, String value, Integer expire) {
        DefaultRedisScript<Object> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_LOCK_CREATE);
        // 脚本中的keys
        List<String> keys = new ArrayList<>();
        keys.add(key);

        Object result = RedisUtil.handler().execute(script, keys, value, expire);
        log.info("上锁结果 {}", result);
        return false;
    }

    public static boolean remove(String key, String value) {
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_LOCK_REMOVE);
        script.setResultType(String.class);
        // 脚本中的keys
        List<String> keys = new ArrayList<>();
        keys.add(key);
        String result = RedisUtil.handler().execute(script, keys, value);
        log.info("解锁结果 {}", result);
        return "OK".equals(result);
    }

}
