package xyz.playedu.api.middleware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/12 10:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
    String key();

    long expire() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
