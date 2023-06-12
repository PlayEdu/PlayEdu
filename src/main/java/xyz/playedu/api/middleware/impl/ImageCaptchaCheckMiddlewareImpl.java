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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.api.request.backend.types.ImageCaptchaRequestInterface;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.JsonResponse;

@Aspect
@Component
@Slf4j
public class ImageCaptchaCheckMiddlewareImpl {
    @Autowired private ImageCaptchaService imageCaptchaService;

    @Pointcut("@annotation(xyz.playedu.api.middleware.ImageCaptchaCheckMiddleware)")
    private void doPointcut() {}

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ImageCaptchaRequestInterface request =
                (ImageCaptchaRequestInterface) joinPoint.getArgs()[0];
        if (!imageCaptchaService.verify(request.getCaptchaKey(), request.getCaptchaValue())) {
            return JsonResponse.error("图形验证码错误");
        }
        return joinPoint.proceed();
    }
}
