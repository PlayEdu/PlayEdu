/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.constant;

public class SystemConstant {

    public static final String VERSION = "v1.0-beta1";

    public static final String ENV_PROD = "prod";

    public static final String REDIS_PREFIX = "playedu:";

    public static final String JWT_PRV_ADMIN_USER =
            "dc14511e97e7eb725fb2976bc939b375"; // AdminUser的md5加密
    public static final String JWT_PRV_USER = "8f9bfe9d1345237cb3b2b205864da075"; // User的md5加密

    public static final String INTERNAL_IP = "127.0.0.1";

    public static final String INTERNAL_IP_AREA = "内网";

    public static final String CONFIG_MASK = "********";
}
