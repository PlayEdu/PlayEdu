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
package xyz.playedu.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.service.AuthService;
import xyz.playedu.api.service.BackendAuthService;

import java.util.HashMap;

@Service
public class BackendAuthServiceImpl implements BackendAuthService {
    @Autowired private AuthService authService;

    @Override
    public String loginUsingId(Integer userId, String loginUrl) {
        return authService.loginUsingId(userId, loginUrl, SystemConstant.JWT_PRV_ADMIN_USER);
    }

    @Override
    public boolean check() {
        return authService.check(SystemConstant.JWT_PRV_ADMIN_USER);
    }

    @Override
    public Integer userId() {
        return authService.userId();
    }

    @Override
    public void logout() {
        authService.logout();
    }

    @Override
    public String jti() {
        return authService.jti();
    }

    @Override
    public HashMap<String, String> parse(String token) {
        return authService.parse(token);
    }
}
