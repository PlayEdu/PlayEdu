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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;
import xyz.playedu.common.constant.SystemConstant;

@Component
public class MemoryCacheUtil {
    private static final Map<String, CacheObject> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final String cacheNamePrefix = SystemConstant.CACHE_NAME_PREFIX;

    static {
        // 启动定时清理过期缓存的线程
        scheduler.scheduleAtFixedRate(
                () -> {
                    cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                },
                1,
                1,
                TimeUnit.MINUTES);
    }

    // 基本操作
    public static void set(String key, Object value) {
        key = cacheNamePrefix + key;
        cache.put(key, new CacheObject(value, Long.MAX_VALUE));
    }

    public static void set(String key, Object value, long expireSeconds) {
        key = cacheNamePrefix + key;
        cache.put(key, new CacheObject(value, System.currentTimeMillis() + expireSeconds * 1000));
    }

    public static void set(String key, Object value, long expire, TimeUnit timeUnit) {
        key = cacheNamePrefix + key;
        cache.put(
                key,
                new CacheObject(value, System.currentTimeMillis() + timeUnit.toMillis(expire)));
    }

    public static Object get(String key) {
        key = cacheNamePrefix + key;
        CacheObject cacheObject = cache.get(key);
        if (cacheObject != null && !cacheObject.isExpired()) {
            return cacheObject.getValue();
        }
        cache.remove(key);
        return null;
    }

    public static Long ttlWithoutPrefix(String key) {
        return getExpire(cacheNamePrefix + key);
    }

    /**
     * 获取所有缓存键值对
     *
     * @return 所有缓存键值对的Map，包含值和失效时间
     */
    public Map<String, Map<String, Object>> getAllCache() {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Map.Entry<String, CacheObject> entry : cache.entrySet()) {
            Map<String, Object> cacheInfo = new HashMap<>();
            cacheInfo.put("value", entry.getValue().getValue());
            cacheInfo.put("expireTime", entry.getValue().getExpireTime());
            cacheInfo.put("ttl", getExpire(entry.getKey()));
            result.put(entry.getKey(), cacheInfo);
        }
        return result;
    }

    /**
     * 获取所有缓存键
     *
     * @return 所有缓存键的列表
     */
    public List<String> getAllKeys() {
        return new ArrayList<>(cache.keySet());
    }

    // 键操作
    public static Boolean exists(String key) {
        key = cacheNamePrefix + key;
        CacheObject cacheObject = cache.get(key);
        return cacheObject != null && !cacheObject.isExpired();
    }

    public static void del(String... key) {
        if (key == null || key.length == 0) {
            return;
        }
        if (key.length == 1) {
            key[0] = cacheNamePrefix + key[0];
            cache.remove(key[0]);
        } else {
            for (String k : key) {
                cache.remove(cacheNamePrefix + k);
            }
        }
    }

    // Hash操作
    public static Object hGet(String key, String field) {
        key = cacheNamePrefix + key;
        CacheObject cacheObject = cache.get(key);
        if (cacheObject != null && !cacheObject.isExpired()) {
            Map<String, Object> map = (Map<String, Object>) cacheObject.getValue();
            return map.get(field);
        }
        return null;
    }

    public static void hSet(String key, String item, Object value) {
        key = cacheNamePrefix + key;
        CacheObject cacheObject = cache.get(key);
        if (cacheObject != null && !cacheObject.isExpired()) {
            Map<String, Object> map = (Map<String, Object>) cacheObject.getValue();
            map.put(item, value);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(item, value);
            cache.put(key, new CacheObject(map, Long.MAX_VALUE));
        }
    }

    // 内部方法
    public static Long increment(String key, long delta, long expireSeconds) {
        key = cacheNamePrefix + key;
        CacheObject cacheObject = cache.get(key);
        if (cacheObject == null || cacheObject.isExpired()) {
            cache.put(
                    key,
                    new CacheObject(
                            new AtomicLong(delta),
                            System.currentTimeMillis() + expireSeconds * 1000));
            return delta;
        }
        // 检查值的类型，如果是Long类型，将其转换为AtomicLong
        Object value = cacheObject.getValue();
        AtomicLong counter;
        if (value instanceof Long) {
            counter = new AtomicLong((Long) value);
            cacheObject.setValue(counter); // 更新缓存对象中的值为AtomicLong类型
        } else if (value instanceof AtomicLong) {
            counter = (AtomicLong) value;
        } else {
            // 如果既不是Long也不是AtomicLong，重新初始化为delta
            counter = new AtomicLong(delta);
            cacheObject.setValue(counter);
        }

        return counter.addAndGet(delta);
    }

    private static Long getExpire(String key) {
        CacheObject cacheObject = cache.get(key);
        if (cacheObject == null) {
            return -2L;
        }
        if (cacheObject.getExpireTime() == Long.MAX_VALUE) {
            return -1L;
        }
        return (cacheObject.getExpireTime() - System.currentTimeMillis()) / 1000;
    }

    private static class CacheObject {
        private Object value;
        private long expireTime;

        public CacheObject(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}
