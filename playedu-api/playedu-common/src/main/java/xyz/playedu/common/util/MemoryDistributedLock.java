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
package xyz.playedu.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class MemoryDistributedLock {
    private static final Map<String, LockInfo> locks = new ConcurrentHashMap<>();

    public boolean tryLock(String key, long expire, TimeUnit timeUnit) {
        LockInfo lockInfo = locks.get(key);
        if (lockInfo == null || lockInfo.isExpired()) {
            locks.put(
                    key,
                    new LockInfo(
                            Thread.currentThread().getId(),
                            System.currentTimeMillis() + timeUnit.toMillis(expire)));
            return true;
        }
        return false;
    }

    public void releaseLock(String key) {
        LockInfo lockInfo = locks.get(key);
        if (lockInfo != null && lockInfo.getThreadId() == Thread.currentThread().getId()) {
            locks.remove(key);
        }
    }

    private static class LockInfo {
        private final long threadId;
        private final long expireTime;

        public LockInfo(long threadId, long expireTime) {
            this.threadId = threadId;
            this.expireTime = expireTime;
        }

        public long getThreadId() {
            return threadId;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}
