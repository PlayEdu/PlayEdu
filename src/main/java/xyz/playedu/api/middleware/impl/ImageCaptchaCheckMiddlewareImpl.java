package xyz.playedu.api.middleware.impl;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.request.backend.types.ImageCaptchaRequestInterface;
import xyz.playedu.api.types.JsonResponse;

@Aspect
@Component
@Slf4j
public class ImageCaptchaCheckMiddlewareImpl {
    @Autowired
    private ImageCaptchaService imageCaptchaService;

    @Pointcut("@annotation(xyz.playedu.api.middleware.ImageCaptchaCheckMiddleware)")
    private void doPointcut() {
    }

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ImageCaptchaRequestInterface request = (ImageCaptchaRequestInterface) joinPoint.getArgs()[0];
        if (!imageCaptchaService.verify(request.getCaptchaKey(), request.getCaptchaValue())) {
            return JsonResponse.error("图形验证码错误");
        }
        return joinPoint.proceed();
    }
}
