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
package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

@Getter
@Setter
public class AdminUserLoginEvent extends ApplicationEvent {

    private Integer adminId;

    private Date loginAt;

    private String token;

    private String ip;

    private Integer loginTimes;

    public AdminUserLoginEvent(
            Object source, Integer adminId, String token, String ip, Integer loginTimes) {
        super(source);
        this.adminId = adminId;
        this.loginAt = new Date();
        this.token = token;
        this.ip = ip;
        this.loginTimes = loginTimes;
    }
}
