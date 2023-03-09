package xyz.playedu.api.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.bus.AppBus;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.RequestUtil;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class AdminMiddleware implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AppBus appBus;

    @Autowired
    private BackendBus backendBus;

    @Autowired
    private AppConfigService configService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 读取全局配置
        Map<String, String> systemConfig = configService.keyValues();
        PlayEduBackendThreadLocal.setConfig(systemConfig);

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

            PlayEduBackendThreadLocal.setAdminUserId(payload.getSub());
            PlayEduBackendThreadLocal.setAdminUser(adminUser);
            PlayEduBackendThreadLocal.setAdminPer(backendBus.adminUserPermissions(adminUser.getId()));

            return HandlerInterceptor.super.preHandle(request, response, handler);
        } catch (Exception e) {
            if (appBus.isDev()) {
                log.debug("jwt解析失败:" + e.getMessage());
            }
            return responseTransform(response, 401, "请重新登录");
        }
    }

    private boolean responseTransform(HttpServletResponse response, int code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error(msg)));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PlayEduBackendThreadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
