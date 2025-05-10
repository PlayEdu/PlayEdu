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
package xyz.playedu.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.common.domain.LdapUser;
import xyz.playedu.common.util.ldap.LdapTransformUser;

/**
 * @author tengyongzhi
 * @description 针对表【ldap_user】的数据库操作Service
 * @createDate 2023-08-31 11:59:27
 */
public interface LdapUserService extends IService<LdapUser> {
    LdapUser findByUUID(String id);

    LdapUser store(LdapTransformUser ldapTransformUser);

    void updateUserId(Integer id, Integer userId);

    void updateCN(Integer id, String cn);

    void updateOU(Integer id, String newOU);

    void updateEmail(Integer id, String email);

    void updateUid(Integer id, String uid);
}
