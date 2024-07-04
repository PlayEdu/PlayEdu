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

import xyz.playedu.common.domain.LdapDepartment;
import xyz.playedu.common.mapper.LdapDepartmentMapper;
import xyz.playedu.common.service.LdapDepartmentService;

import java.util.Date;
import java.util.List;

@Service
public class LdapDepartmentServiceImpl extends ServiceImpl<LdapDepartmentMapper, LdapDepartment>
        implements LdapDepartmentService {

    @Override
    public List<LdapDepartment> all() {
        return list();
    }

    @Override
    public List<LdapDepartment> notChunkByUUIDList(List<String> uuidList) {
        return list(query().getWrapper().notIn("uuid", uuidList));
    }

    @Override
    public void destroy(Integer id) {
        remove(query().getWrapper().eq("id", id));
    }

    @Override
    public void create(Integer depId, String uuid, String dn) {
        LdapDepartment ldapDepartment = new LdapDepartment();
        ldapDepartment.setDepartmentId(depId);
        ldapDepartment.setDn(dn);
        ldapDepartment.setUuid(uuid);
        ldapDepartment.setCreatedAt(new Date());
        ldapDepartment.setUpdatedAt(new Date());

        save(ldapDepartment);
    }

    @Override
    public void updateDnById(Integer id, String dn) {
        LdapDepartment ldapDepartment = new LdapDepartment();
        ldapDepartment.setId(id);
        ldapDepartment.setDn(dn);
        updateById(ldapDepartment);
    }
}
