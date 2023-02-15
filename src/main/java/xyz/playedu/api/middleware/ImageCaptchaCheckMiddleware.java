package xyz.playedu.api.middleware;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageCaptchaCheckMiddleware {
}
