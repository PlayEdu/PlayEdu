package xyz.playedu.api;

import java.util.LinkedHashMap;

public class PlayEduThreadLocal {

    private static final java.lang.ThreadLocal<LinkedHashMap<String, Object>> THREAD_LOCAL = new java.lang.ThreadLocal<>();

    public PlayEduThreadLocal() {

    }

    /**
     * 写入变量
     *
     * @param key
     * @param val
     */
    public static void put(String key, Object val) {
        LinkedHashMap<String, Object> hashMap = THREAD_LOCAL.get();
        if (hashMap == null) {
            hashMap = new LinkedHashMap<>();
        }
        hashMap.put(key, val);
        THREAD_LOCAL.set(hashMap);
    }

    public static Object get(String key) {
        return THREAD_LOCAL.get().getOrDefault(key, null);
    }

    public static Integer getAdminUserID() {
        return (Integer) get("admin_user_id");
    }

    public static void setAdminUserId(Integer userId) {
        put("admin_user_id", userId);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
