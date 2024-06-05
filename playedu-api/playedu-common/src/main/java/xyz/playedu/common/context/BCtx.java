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
package xyz.playedu.common.context;

import xyz.playedu.common.domain.AdminUser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BCtx {

    private static final ThreadLocal<LinkedHashMap<String, Object>> THREAD_LOCAL =
            new ThreadLocal<>();

    public static final String KEY_ADMIN_USER_ID = "admin_id";
    public static final String KEY_ADMIN_USER = "admin_user";
    public static final String KEY_ADMIN_PER = "admin_per";
    public static final String KEY_CONFIG = "config";

    public BCtx() {}

    private static void put(String key, Object val) {
        LinkedHashMap<String, Object> hashMap = THREAD_LOCAL.get();
        if (hashMap == null) {
            hashMap = new LinkedHashMap<>();
        }
        hashMap.put(key, val);
        THREAD_LOCAL.set(hashMap);
    }

    private static Object get(String key) {
        if (THREAD_LOCAL.get() == null) {
            return null;
        }
        return THREAD_LOCAL.get().getOrDefault(key, null);
    }

    public static boolean isNull() {
        return THREAD_LOCAL.get() == null;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static Integer getId() {
        return (Integer) get(KEY_ADMIN_USER_ID);
    }

    public static void setId(Integer userId) {
        put(KEY_ADMIN_USER_ID, userId);
    }

    public static AdminUser getAdminUser() {
        return (AdminUser) get(KEY_ADMIN_USER);
    }

    public static void setAdminUser(AdminUser adminUser) {
        put(KEY_ADMIN_USER, adminUser);
    }

    public static void setAdminPer(HashMap<String, Boolean> permissions) {
        put(KEY_ADMIN_PER, permissions);
    }

    public static HashMap<String, Boolean> getAdminPer() {
        return (HashMap<String, Boolean>) get(KEY_ADMIN_PER);
    }

    public static void setConfig(Map<String, String> config) {
        put(KEY_CONFIG, config);
    }

    public static Map<String, String> getConfig() {
        return (Map<String, String>) get(KEY_CONFIG);
    }
}
