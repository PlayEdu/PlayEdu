package xyz.playedu.api.middleware.impl;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.middleware.AuthMiddleware;
import xyz.playedu.api.service.JWTService;

@Aspect
@Component
@Slf4j
public class AuthMiddlewareImpl {

    @Autowired
    private JWTService jwtService;

    @Pointcut("@annotation(xyz.playedu.api.middleware.AuthMiddleware)")
    private void doPointcut() {
    }

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AuthMiddleware authMiddleware = methodSignature.getMethod().getAnnotation(AuthMiddleware.class);
        log.info("prv的值:" + authMiddleware.prv());
        return joinPoint.proceed();
    }

}
