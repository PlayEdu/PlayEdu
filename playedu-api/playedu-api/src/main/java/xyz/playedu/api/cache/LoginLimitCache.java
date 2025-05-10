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
package xyz.playedu.api.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.config.PlayEduConfig;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.RateLimiterService;
import xyz.playedu.common.util.MemoryCacheUtil;

@Component
public class LoginLimitCache {

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private PlayEduConfig playEduConfig;

    public void check(String email) throws ServiceException {
        String limitKey = cacheKey(email);
        Long reqCount = rateLimiterService.current(limitKey, 600L);
        if (reqCount >= 10 && !playEduConfig.getTesting()) {
            Long exp = MemoryCacheUtil.ttlWithoutPrefix(limitKey);
            String msg = String.format("您的账号已被锁定，请%s后重试", exp > 60 ? exp / 60 + "分钟" : exp + "秒");
            throw new ServiceException(msg);
        }
    }

    public void destroy(String email) {
        MemoryCacheUtil.del(cacheKey(email));
    }

    private String cacheKey(String email) {
        return "login-limit:" + email;
    }
}
