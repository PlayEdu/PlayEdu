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

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/12 10:45
 */
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
