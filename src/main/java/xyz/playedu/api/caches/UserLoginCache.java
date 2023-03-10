package xyz.playedu.api.caches;

import org.springframework.stereotype.Component;
import xyz.playedu.api.exception.LimitException;
import xyz.playedu.api.util.RedisUtil;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/10 14:13
 */
@Component
public class UserLoginCache {

    private final static String keyTemplate = "user-login:%s";

    private final static int expire = 10;//10s

    public void check(String email) throws LimitException {
        if (RedisUtil.exists(key(email))) {
            throw new LimitException();
        }
    }

    public void put(String email) {
        RedisUtil.set(key(email), "1", expire);
    }

    private String key(String email) {
        return String.format(keyTemplate, email);
    }

}
