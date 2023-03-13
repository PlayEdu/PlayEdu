package xyz.playedu.api;

import xyz.playedu.api.domain.User;

import java.util.LinkedHashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:24
 */
public class PlayEduFContext {
    private static final java.lang.ThreadLocal<LinkedHashMap<String, Object>> THREAD_LOCAL = new java.lang.ThreadLocal<>();

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER = "user";

    public PlayEduFContext() {
    }

    private static void put(String key, Object val) {
        LinkedHashMap<String, Object> hashMap = THREAD_LOCAL.get();
        if (hashMap == null) {
            hashMap = new LinkedHashMap<>();
        }
        hashMap.put(key, val);
        THREAD_LOCAL.set(hashMap);
    }

    private static Object get(String key) {
        return THREAD_LOCAL.get().getOrDefault(key, null);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static void setUserId(Integer id) {
        put(KEY_USER_ID, id);
    }

    public static Integer getUserId() {
        return (Integer) get(KEY_USER_ID);
    }

    public static void setUser(User user) {
        put(KEY_USER, user);
    }

    public static User getUser() {
        return (User) get(KEY_USER);
    }
}
