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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.util.RedisUtil;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.course.bus.UserBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 15:20
 */
@Component
public class UserCanSeeCourseCache {

    @Autowired private UserBus userBus;

    private static final String keyTemplate = "c:%d-u:%d";

    private static final int expire = 3600; // s

    public boolean check(Integer userId, Integer courseId, boolean isThrow)
            throws ServiceException {
        boolean result;
        if (RedisUtil.exists(key(userId, courseId))) {
            String cacheResult = (String) RedisUtil.get(key(userId, courseId));
            result = "1".equals(cacheResult);
        } else {
            result = userBus.canSeeCourse(userId, courseId);
            put(userId, courseId, result);
        }
        if (!result && isThrow) {
            throw new ServiceException("无权限观看");
        }
        return result;
    }

    public void put(Integer userId, Integer courseId, boolean result) {
        RedisUtil.set(key(userId, courseId), result ? "1" : "0", expire);
    }

    public void destroy(List<Integer> userIds, Integer courseId) {
        if (StringUtil.isNotEmpty(userIds)) {
            List<String> keyList = new ArrayList<>();
            for (Integer userId : userIds) {
                keyList.add(key(userId, courseId));
            }
            RedisUtil.del(keyList.toArray(new String[keyList.size()]));
        }
    }

    private String key(Integer userId, Integer courseId) {
        return String.format(keyTemplate, courseId, userId);
    }
}
