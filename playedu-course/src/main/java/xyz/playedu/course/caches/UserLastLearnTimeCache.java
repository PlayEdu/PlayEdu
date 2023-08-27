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
package xyz.playedu.course.caches;

import org.springframework.stereotype.Component;

import xyz.playedu.common.util.RedisUtil;

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
