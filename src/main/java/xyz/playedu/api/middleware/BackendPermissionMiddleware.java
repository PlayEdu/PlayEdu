/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.middleware;

import java.lang.annotation.*;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/21 16:40
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BackendPermissionMiddleware {
    String slug() default "";
}
