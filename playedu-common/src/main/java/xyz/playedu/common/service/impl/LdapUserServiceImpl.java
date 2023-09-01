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
package xyz.playedu.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.common.domain.LdapUser;
import xyz.playedu.common.mapper.LdapUserMapper;
import xyz.playedu.common.service.LdapUserService;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.common.util.ldap.LdapTransformUser;

import java.util.Date;

/**
 * @author tengyongzhi
 * @description 针对表【ldap_user】的数据库操作Service实现
 * @createDate 2023-08-31 11:59:27
 */
@Service
public class LdapUserServiceImpl extends ServiceImpl<LdapUserMapper, LdapUser>
        implements LdapUserService {

    @Override
    public LdapUser findByUUID(String id) {
        return getOne(query().getWrapper().eq("uuid", id));
    }

    @Override
    public LdapUser store(LdapTransformUser ldapTransformUser) {
        LdapUser user = new LdapUser();
        user.setUuid(ldapTransformUser.getId());
        user.setCn(ldapTransformUser.getCn());
        user.setDn(ldapTransformUser.getDn());
        user.setUid(ldapTransformUser.getUid());
        // ou
        user.setOu(String.join(",", ldapTransformUser.getOu()));
        // 邮箱可能不存在
        if (StringUtil.isNotEmpty(ldapTransformUser.getEmail())) {
            user.setEmail(ldapTransformUser.getEmail());
        }
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        save(user);

        return user;
    }

    @Override
    public void updateUserId(Integer id, Integer userId) {
        LdapUser user = new LdapUser();
        user.setId(id);
        user.setUserId(userId);
        updateById(user);
    }

    @Override
    public void updateCN(Integer id, String cn) {
        LdapUser user = new LdapUser();
        user.setId(id);
        user.setCn(cn == null ? "" : cn);
        updateById(user);
    }

    @Override
    public void updateOU(Integer id, String newOU) {
        LdapUser user = new LdapUser();
        user.setId(id);
        user.setOu(newOU == null ? "" : newOU);
        updateById(user);
    }

    @Override
    public void updateEmail(Integer id, String email) {
        LdapUser user = new LdapUser();
        user.setId(id);
        user.setEmail(email == null ? "" : email);
        updateById(user);
    }

    @Override
    public void updateUid(Integer id, String uid) {
        LdapUser user = new LdapUser();
        user.setId(id);
        user.setUid(uid);
        updateById(user);
    }
}
