/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.api.FCtx;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.api.event.UserLogoutEvent;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.exception.LimitException;
import xyz.playedu.api.middleware.ImageCaptchaCheckMiddleware;
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
 *
 * @create 2023/3/2 21:51
 */
@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {

    @Autowired private UserService userService;

    @Autowired private JWTService jwtService;

    @Autowired private ApplicationContext ctx;

    @PostMapping("/password")
    @ImageCaptchaCheckMiddleware
    public JsonResponse password(@RequestBody @Validated LoginPasswordRequest req)
            throws LimitException {
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

        JwtToken token =
                jwtService.generate(user.getId(), RequestUtil.url(), SystemConstant.JWT_PRV_USER);

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token.getToken());
        data.put("expired", token.getExpire());

        ctx.publishEvent(
                new UserLoginEvent(
                        this,
                        user.getId(),
                        user.getEmail(),
                        token.getToken(),
                        IpUtil.getIpAddress(),
                        RequestUtil.ua()));

        return JsonResponse.data(data);
    }

    @PostMapping("/logout")
    public JsonResponse logout() throws JwtLogoutException {
        jwtService.userLogout(RequestUtil.token());
        ctx.publishEvent(new UserLogoutEvent(this, FCtx.getId(), FCtx.getJwtJti()));
        return JsonResponse.success();
    }
}
