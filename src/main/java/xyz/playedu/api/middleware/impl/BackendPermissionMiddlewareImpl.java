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

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;

@Aspect
@Component
@Slf4j
public class BackendPermissionMiddlewareImpl {

    @Autowired private BackendBus backendBus;

    @Pointcut("@annotation(xyz.playedu.api.middleware.BackendPermissionMiddleware)")
    private void doPointcut() {}

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        BackendPermissionMiddleware middleware =
                signature.getMethod().getAnnotation(BackendPermissionMiddleware.class);
        Integer adminUserId = BCtx.getId();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(adminUserId);
        if (permissions.get(middleware.slug()) == null) {
            return JsonResponse.error("权限不足", 403);
        }
        return joinPoint.proceed();
    }
}
