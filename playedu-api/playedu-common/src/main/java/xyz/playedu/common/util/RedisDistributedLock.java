/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/12 10:43
 */
@Component
public class RedisDistributedLock {

    private final StringRedisTemplate redisTemplate;
    private final ThreadLocal<String> lockValue = new ThreadLocal<>();

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(String key, long expire, TimeUnit timeUnit) {
        String value = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
        if (Boolean.TRUE.equals(success)) {
            lockValue.set(value);
            return true;
        }
        return false;
    }

    public boolean releaseLock(String key) {
        String value = lockValue.get();
        if (value == null) {
            return false;
        }
        DefaultRedisScript<Boolean> script =
                new DefaultRedisScript<>(
                        "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',"
                                + " KEYS[1]) else return 0 end",
                        Boolean.class);
        Boolean success = redisTemplate.execute(script, Collections.singletonList(key), value);
        if (Boolean.TRUE.equals(success)) {
            lockValue.remove();
            return true;
        }
        return false;
    }
}
