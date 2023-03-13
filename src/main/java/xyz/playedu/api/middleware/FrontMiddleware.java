package xyz.playedu.api.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.playedu.api.PlayEduFContext;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

import java.io.IOException;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:40
 */
@Component
@Slf4j
public class FrontMiddleware implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        return false;
    }

    private boolean responseTransform(HttpServletResponse response, int code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error(msg)));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PlayEduFContext.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
