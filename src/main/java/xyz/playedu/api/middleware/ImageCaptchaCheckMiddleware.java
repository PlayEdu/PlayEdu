package xyz.playedu.api.middleware;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageCaptchaCheckMiddleware {
}
