package xyz.playedu.api.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.playedu.api.middleware.AdminAuthMiddleware;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AdminAuthMiddleware adminAuthMiddleware;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthMiddleware).addPathPatterns("/backend/**");
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
