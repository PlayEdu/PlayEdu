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
package xyz.playedu.api.middleware.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import xyz.playedu.api.exception.LimitException;
import xyz.playedu.api.middleware.Lock;
import xyz.playedu.api.util.RedisDistributedLock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LockImpl {
    private final RedisDistributedLock redisDistributedLock;

    public LockImpl(RedisDistributedLock redisDistributedLock) {
        this.redisDistributedLock = redisDistributedLock;
    }

    @Around("@annotation(xyz.playedu.api.middleware.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Lock distributedLock = method.getAnnotation(Lock.class);
        String key = distributedLock.key();
        long expire = distributedLock.expire();
        TimeUnit timeUnit = distributedLock.timeUnit();
        boolean success = redisDistributedLock.tryLock(key, expire, timeUnit);
        if (!success) {
            throw new LimitException("请稍后再试");
        }
        try {
            return joinPoint.proceed();
        } finally {
            redisDistributedLock.releaseLock(key);
        }
    }
}
