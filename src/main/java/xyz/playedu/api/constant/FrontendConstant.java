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
package xyz.playedu.api.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/13 14:07
 */
public class FrontendConstant {

    public static final List<String> UN_AUTH_URI_WHITELIST =
            new ArrayList<>() {
                {
                    add("/api/v1/system/config");
                    add("/api/v1/system/image-captcha");
                    add("/api/v1/auth/login/password");
                }
            };

    public static final String USER_UPLOAD_IMAGE_TYPE_AVATAR = "avatar";

    public static final String USER_UPLOAD_IMAGE_SCENE_AVATAR = "avatar";

    public static final String DIR_AVATAR = "user/avatar/";
}
