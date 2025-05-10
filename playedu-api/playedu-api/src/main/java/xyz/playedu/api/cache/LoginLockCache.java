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

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.util.MemoryDistributedLock;

@Component
public class LoginLockCache {

    @Autowired private MemoryDistributedLock distributedLock;

    public boolean apply(String username) {
        String key = cacheKey(username);
        return distributedLock.tryLock(key, 10L, TimeUnit.SECONDS);
    }

    public void release(String username) {
        distributedLock.releaseLock(cacheKey(username));
    }

    private String cacheKey(String username) {
        return "login-lock:" + username;
    }
}
