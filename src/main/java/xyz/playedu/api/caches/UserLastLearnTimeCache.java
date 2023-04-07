/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.caches;

import org.springframework.stereotype.Component;

import xyz.playedu.api.util.RedisUtil;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/22 13:57
 */
@Component
public class UserLastLearnTimeCache {

    private static final String groupName = "user-learn-last-timestamp";

    private static final int expire = 9500; // 9.5s

    public Long get(Integer userId) {
        return (Long) RedisUtil.hGet(groupName, userId + "");
    }

    public void put(Integer userId, Long timestamp) {
        RedisUtil.hSet(groupName, userId + "", timestamp);
    }
}
