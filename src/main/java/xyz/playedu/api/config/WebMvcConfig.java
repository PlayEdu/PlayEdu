/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.config;

import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import xyz.playedu.api.middleware.AdminMiddleware;
import xyz.playedu.api.middleware.FrontMiddleware;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource private AdminMiddleware adminMiddleware;

    @Autowired private FrontMiddleware frontMiddleware;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminMiddleware).addPathPatterns("/backend/**");
        registry.addInterceptor(frontMiddleware).addPathPatterns("/api/v1/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(false)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE")
                .exposedHeaders("*");
    }
}
