package xyz.playedu.api.middleware;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.playedu.api.PlayEduThreadLocal;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.RequestUtil;

import java.io.IOException;

@Component
@Slf4j
public class AdminAuthMiddleware implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (BackendBus.inUnAuthWhitelist(request.getRequestURI())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String token = RequestUtil.token();
        if (token.length() == 0) {
            return responseTransform(response, 401, "请登录");
        }

        try {
            JWTPayload payload = jwtService.parse(token, SystemConstant.JWT_PRV_ADMIN_USER);

            AdminUser adminUser = adminUserService.findById(payload.getSub());
            if (adminUser == null) {
                return responseTransform(response, 404, "管理员不存在");
            }
            if (adminUser.getIsBanLogin() == 1) {
                return responseTransform(response, 403, "当前管理员禁止登录");
            }

            PlayEduThreadLocal.setAdminUserId(payload.getSub());
            PlayEduThreadLocal.setAdminUser(adminUser);

            return HandlerInterceptor.super.preHandle(request, response, handler);
        } catch (Exception e) {
            responseTransform(response, 401, "请重新登录");
            return false;
        }
    }

    private boolean responseTransform(HttpServletResponse response, int code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(JsonResponse.error(msg)));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PlayEduThreadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
