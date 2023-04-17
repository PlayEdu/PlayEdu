/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.util;

import jakarta.annotation.Resource;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import xyz.playedu.api.constant.SystemConstant;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;
    private static final String redisPrefix = SystemConstant.REDIS_PREFIX;

    /**
     * 注入Redis
     *
     * @param redisTemplate Redis对象
     * @author fzr
     */
    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 对象句柄
     *
     * @return RedisTemplate
     * @author fzr
     */
    public static RedisTemplate<String, Object> handler() {
        return redisTemplate;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param second 时间(秒)
     * @author fzr
     */
    public static void expire(String key, Long second) {
        key = redisPrefix + key;
        redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param millisecond 时间(毫秒)
     * @author fzr
     */
    public static void pExpire(String key, Long millisecond) {
        key = redisPrefix + key;
        redisTemplate.expire(key, millisecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 指定缓存永久有效
     *
     * @param key 键
     * @author fzr
     */
    public static void persist(String key) {
        key = redisPrefix + key;
        redisTemplate.persist(key);
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键不能为null
     * @return 返回0代表为永久有效(秒)
     * @author fzr
     */
    public static Long ttl(String key) {
        key = redisPrefix + key;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键不能为null
     * @return 返回0代表为永久有效(毫秒)
     * @author fzr
     */
    public static Long pTtl(String key) {
        key = redisPrefix + key;
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true=存在,false=不存在
     * @author fzr
     */
    public static Boolean exists(String key) {
        key = redisPrefix + key;
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除1个或多个键
     *
     * @param key 键(一个或多个)
     * @author fzr
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        if (key.length == 1) {
            key[0] = redisPrefix + key[0];
            redisTemplate.delete(key[0]);
        } else {
            for (int i = 0; key.length > i; i++) {
                key[i] = redisPrefix + key[i];
            }
            redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
        }
    }

    /**
     * 给key赋值一个新的key名
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     * @author fzr
     */
    public static void rename(String oldKey, String newKey) {
        oldKey = redisPrefix + oldKey;
        newKey = redisPrefix + newKey;
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 将当前数据库的key移动到给定的数据库db当中
     *
     * @param key 键
     * @param db 库
     * @return Boolean
     * @author fzr
     */
    public static Boolean move(String key, int db) {
        key = redisPrefix + key;
        return redisTemplate.move(key, db);
    }

    /**
     * 获取匹配的key值
     *
     * @param pattern 通配符(*, ?, [])
     * @return Set
     * @author fzr
     * @author fzr
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 随机返回一个key
     *
     * @return String
     * @author fzr
     * @author fzr
     */
    public static String randomKey() {
        return redisTemplate.randomKey();
    }

    /* ***************** common end *************** */

    /**
     * 按匹配获取或有KEY
     *
     * @param pattern 规则
     * @return Set<String>
     * @author fzr
     */
    public static Set<String> matchSet(String pattern) {
        Set<String> keys = new LinkedHashSet<>();
        RedisUtil.handler()
                .execute(
                        (RedisConnection connection) -> {
                            try (Cursor<byte[]> cursor =
                                    connection.scan(
                                            ScanOptions.scanOptions()
                                                    .count(Long.MAX_VALUE)
                                                    .match(pattern)
                                                    .build())) {
                                cursor.forEachRemaining(
                                        item -> {
                                            keys.add(RedisSerializer.string().deserialize(item));
                                        });
                                return null;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

        return keys;
    }

    /**
     * 获取key的值
     *
     * @param key 键
     * @return Object
     * @author fzr
     */
    public static Object get(String key) {
        key = redisPrefix + key;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取旧值并设置新值
     *
     * @param key 键
     * @param newVal 新值
     * @return Object
     * @author fzr
     */
    public static Object getSet(String key, Object newVal) {
        key = redisPrefix + key;
        return redisTemplate.opsForValue().getAndSet(key, newVal);
    }

    /**
     * 设置键值对
     *
     * @param key 键
     * @param value 值
     * @author fzr
     */
    public static void set(String key, Object value) {
        key = redisPrefix + key;
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置键值对并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time time要大于0 如果time小于等于0 将设置无限期
     * @author fzr
     */
    public static void set(String key, Object value, long time) {
        key = redisPrefix + key;
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return Long
     * @author fzr
     */
    public static Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        key = redisPrefix + key;
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return Long
     * @author fzr
     */
    public static Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        key = redisPrefix + key;
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /* ***************** String end *************** */

    /**
     * 获取key中field域的值
     *
     * @param key 键 不能为null
     * @param field 项 不能为null
     * @return 值
     * @author fzr
     */
    public static Object hGet(String key, String field) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 判断key中有没有field域名
     *
     * @param key 键
     * @param field 字段
     * @return Boolean
     * @author fzr
     */
    public static Boolean hExists(String key, Object field) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     * @author fzr
     */
    public Map<Object, Object> hmGet(String key) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置field1->N个域,对应的值是value1->N
     *
     * @param key 键
     * @param map 对应多个键值
     * @author fzr
     */
    public static void hmSet(String key, Map<String, Object> map) {
        key = redisPrefix + key;
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @author fzr
     */
    public static void hmSet(String key, Map<String, Object> map, long time) {
        key = redisPrefix + key;
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     * @author fzr
     */
    public static void hSet(String key, String item, Object value) {
        key = redisPrefix + key;
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     * @author fzr
     */
    public static boolean hSet(String key, String item, Object value, long time) {
        key = redisPrefix + key;
        redisTemplate.opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    /**
     * 删除hash表中的值
     *
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     * @author fzr
     */
    public static void hDel(String key, Object... item) {
        key = redisPrefix + key;
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     * @author fzr
     */
    public static boolean hHasKey(String key, String item) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个并把新增后的值返回
     *
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return double
     * @author fzr
     */
    public static double hIncr(String key, String item, long by) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return double
     * @author fzr
     */
    public static double hDecr(String key, String item, long by) {
        key = redisPrefix + key;
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /* ***************** Map end *************** */

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set
     * @author fzr
     */
    public static Set<Object> sGet(String key) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     * @author fzr
     */
    public Boolean sHasKey(String key, Object value) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     * @author fzr
     */
    public static Long sSet(String key, Object... values) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     * @author fzr
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return Long
     * @author fzr
     */
    public Long sGetSetSize(String key) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     * @author fzr
     */
    public Long setRemove(String key, Object... values) {
        key = redisPrefix + key;
        return redisTemplate.opsForSet().remove(key, values);
    }

    /* ***************** Set end *************** */

    /**
     * 获取list缓存的内容
     *
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return List
     * @author fzr
     */
    public List<Object> lGet(String key, long start, long end) {
        key = redisPrefix + key;
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return Long
     * @author fzr
     */
    public Long lGetListSize(String key) {
        key = redisPrefix + key;
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key 键
     * @param index 索引 index>=0时,0 表头，1 第二个元素,依次类推;index<0时,-1,表尾,-2倒数第二个元素，依次类推
     * @return Object
     * @author fzr
     */
    public Object lGetIndex(String key, long index) {
        key = redisPrefix + key;
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     * @author fzr
     */
    public boolean lSet(String key, Object value) {
        key = redisPrefix + key;
        redisTemplate.opsForList().rightPush(key, value);
        return true;
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param second 时间(秒)
     * @return boolean
     * @author fzr
     */
    public boolean lSet(String key, Object value, long second) {
        key = redisPrefix + key;
        redisTemplate.opsForList().rightPush(key, value);
        if (second > 0) expire(key, second);
        return true;
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean
     * @author fzr
     */
    public boolean lSet(String key, List<Object> value) {
        key = redisPrefix + key;
        redisTemplate.opsForList().rightPushAll(key, value);
        return true;
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return boolean
     * @author fzr
     */
    public boolean lSet(String key, List<Object> value, Long time) {
        key = redisPrefix + key;
        redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0) expire(key, time);
        return true;
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return boolean
     * @author fzr
     */
    public boolean lUpdateIndex(String key, Long index, Object value) {
        key = redisPrefix + key;
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    /**
     * 移除N个值为value
     *
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     * @author fzr
     */
    public Long lRemove(String key, Long count, Object value) {
        key = redisPrefix + key;
        return redisTemplate.opsForList().remove(key, count, value);
    }
}
