package xyz.playedu.api.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.playedu.api.PlayEduFContext;
import xyz.playedu.api.constant.FrontendConstant;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.RequestUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:40
 */
@Component
@Slf4j
public class FrontMiddleware implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        if (Arrays.stream(FrontendConstant.UN_AUTH_URI_WHITELIST).toList().contains(request.getRequestURI())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String token = RequestUtil.token();
        if (token.length() == 0) {
            return responseTransform(response, 401, "请登录");
        }

        try {
            JWTPayload payload = jwtService.parse(token, SystemConstant.JWT_PRV_ADMIN_USER);

            User user = userService.find(payload.getSub());
            if (user == null) {
                return responseTransform(response, 404, "管理员不存在");
            }
            if (user.getIsLock() == 1) {
                return responseTransform(response, 403, "当前学员已锁定");
            }

            PlayEduFContext.setUserId(user.getId());
            PlayEduFContext.setUser(user);

            return HandlerInterceptor.super.preHandle(request, response, handler);
        } catch (Exception e) {
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
        PlayEduFContext.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
