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
package xyz.playedu.api.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.api.bus.UserBus;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.util.RedisUtil;

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

    public boolean check(User user, Course course, boolean isThrow) throws ServiceException {
        boolean result;
        if (RedisUtil.exists(key(user, course))) {
            String cacheResult = (String) RedisUtil.get(key(user, course));
            result = "1".equals(cacheResult);
        } else {
            result = userBus.canSeeCourse(user, course);
            put(user, course, result);
        }
        if (!result && isThrow) {
            throw new ServiceException("无权限观看");
        }
        return result;
    }

    public void put(User user, Course course, boolean result) {
        RedisUtil.set(key(user, course), result ? "1" : "0", expire);
    }

    private String key(User user, Course course) {
        return String.format(keyTemplate, course.getId(), user.getId());
    }
}
