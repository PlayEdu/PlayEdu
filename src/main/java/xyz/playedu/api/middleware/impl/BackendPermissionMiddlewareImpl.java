package xyz.playedu.api.middleware.impl;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/21 16:42
 */
@Aspect
@Component
@Slf4j
public class BackendPermissionMiddlewareImpl {

    @Autowired
    private BackendBus backendBus;

    @Pointcut("@annotation(xyz.playedu.api.middleware.BackendPermissionMiddleware)")
    private void doPointcut() {
    }

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        BackendPermissionMiddleware middleware = signature.getMethod().getAnnotation(BackendPermissionMiddleware.class);
        Integer adminUserId = PlayEduBackendThreadLocal.getAdminUserID();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(adminUserId);
        if (permissions.get(middleware.slug()) == null) {
            return JsonResponse.error("权限不足", 403);
        }
        return joinPoint.proceed();
    }
}
