package xyz.playedu.api.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.PlayEduFCtx;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.api.event.UserLogoutEvent;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.exception.LimitException;
import xyz.playedu.api.request.frontend.LoginPasswordRequest;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.JwtToken;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.IpUtil;
import xyz.playedu.api.util.RequestUtil;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/2 21:51
 */
@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext ctx;

    @PostMapping("/password")
    public JsonResponse password(@RequestBody @Validated LoginPasswordRequest req) throws LimitException {
        String email = req.getEmail();

        User user = userService.find(email);
        if (user == null) {
            return JsonResponse.error("邮箱或密码错误");
        }
        if (!HelperUtil.MD5(req.getPassword() + user.getSalt()).equals(user.getPassword())) {
            return JsonResponse.error("邮箱或密码错误");
        }
        if (user.getIsLock() == 1) {
            return JsonResponse.error("当前学员已锁定无法登录");
        }

        JwtToken token = jwtService.generate(user.getId(), RequestUtil.url(), SystemConstant.JWT_PRV_USER);

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token.getToken());
        data.put("expired", token.getExpire());

        ctx.publishEvent(new UserLoginEvent(this, user.getId(), user.getEmail(), token.getToken(), IpUtil.getIpAddress(), RequestUtil.ua()));

        return JsonResponse.data(data);
    }

    @PostMapping("/logout")
    public JsonResponse logout() throws JwtLogoutException {
        jwtService.userLogout(PlayEduFCtx.getToken());
        ctx.publishEvent(new UserLogoutEvent(this, PlayEduFCtx.getUserId(), PlayEduFCtx.getJwtJti()));
        return JsonResponse.success();
    }

}
