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

import java.util.LinkedHashMap;
import xyz.playedu.common.domain.User;

public class FCtx {
    private static final ThreadLocal<LinkedHashMap<String, Object>> THREAD_LOCAL =
            new ThreadLocal<>();

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER = "user";
    private static final String KEY_JWT_JTI = "jwt_jti";

    public FCtx() {}

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

    public static void setId(Integer id) {
        put(KEY_USER_ID, id);
    }

    public static Integer getId() {
        return (Integer) get(KEY_USER_ID);
    }

    public static void setUser(User user) {
        put(KEY_USER, user);
    }

    public static User getUser() {
        return (User) get(KEY_USER);
    }

    public static void setJWtJti(String jti) {
        put(KEY_JWT_JTI, jti);
    }

    public static String getJwtJti() {
        return (String) get(KEY_JWT_JTI);
    }
}
