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
package xyz.playedu.system.aspectj;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.annotation.Lock;
import xyz.playedu.common.exception.LimitException;
import xyz.playedu.common.util.MemoryDistributedLock;

@Aspect
@Component
public class LockAspect {
    @Autowired private MemoryDistributedLock distributedLock;

    public LockAspect(MemoryDistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    @Around("@annotation(xyz.playedu.common.annotation.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String key = lock.key();
        long expire = lock.expire();
        TimeUnit timeUnit = lock.timeUnit();
        boolean success = distributedLock.tryLock(key, expire, timeUnit);
        if (!success) {
            throw new LimitException("请稍后再试");
        }
        try {
            return joinPoint.proceed();
        } finally {
            distributedLock.releaseLock(key);
        }
    }
}
