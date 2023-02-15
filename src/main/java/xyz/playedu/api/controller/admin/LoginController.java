package xyz.playedu.api.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.middleware.ImageCaptchaCheckMiddleware;
import xyz.playedu.api.request.LoginRequest;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.MD5Util;

@RestController
@RequestMapping("/admin/v1/auth")
public class LoginController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/login")
    @ImageCaptchaCheckMiddleware
    public JsonResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.findByEmail(loginRequest.email);
        if (adminUser == null) {
            return JsonResponse.error("邮箱不存在");
        }
        String password = MD5Util.md5(loginRequest.getPassword() + adminUser.getSalt());
        if (password != adminUser.getPassword()) {
            return JsonResponse.error("密码错误");
        }
        if (adminUser.getIsBanLogin() == 1) {
            return JsonResponse.error("当前用户禁止登录");
        }
        return JsonResponse.success("success");
    }

    @PostMapping("/logout")
    public JsonResponse logout() {
        return JsonResponse.success("success");
    }

}
