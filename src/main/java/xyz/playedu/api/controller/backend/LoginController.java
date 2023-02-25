package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.middleware.ImageCaptchaCheckMiddleware;
import xyz.playedu.api.request.backend.LoginRequest;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.JwtToken;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.IpUtil;
import xyz.playedu.api.util.RequestUtil;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/backend/v1/auth")
public class LoginController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @PostMapping("/login")
    @ImageCaptchaCheckMiddleware
    public JsonResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.findByEmail(loginRequest.email);
        if (adminUser == null) {
            return JsonResponse.error("邮箱或密码错误");
        }
        String password = HelperUtil.MD5(loginRequest.getPassword() + adminUser.getSalt()).toLowerCase();
        if (!adminUser.getPassword().equals(password)) {
            return JsonResponse.error("邮箱或密码错误");
        }
        if (adminUser.getIsBanLogin() == 1) {
            return JsonResponse.error("当前用户已禁止登录");
        }

        String url = RequestUtil.url();
        JwtToken token = jwtService.generate(adminUser.getId(), url, SystemConstant.JWT_PRV_ADMIN_USER);

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token.getToken());
        data.put("expire", token.getExpire());

        context.publishEvent(new AdminUserLoginEvent(this, adminUser.getId(), new Date(), token.getToken(), IpUtil.getIpAddress(), adminUser.getLoginTimes()));

        return JsonResponse.data(data);
    }

    @PostMapping("/logout")
    public JsonResponse logout() throws JwtLogoutException {
        jwtService.logout(RequestUtil.token(), SystemConstant.JWT_PRV_ADMIN_USER);
        return JsonResponse.success("success");
    }

}
