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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.playedu.common.domain.LdapSyncUserDetail;
import xyz.playedu.common.mapper.LdapSyncUserDetailMapper;
import xyz.playedu.common.service.LdapSyncUserDetailService;

/** LDAP用户同步详情服务实现类 */
@Service
public class LdapSyncUserDetailServiceImpl
        extends ServiceImpl<LdapSyncUserDetailMapper, LdapSyncUserDetail>
        implements LdapSyncUserDetailService {

    @Override
    public void batchCreate(List<LdapSyncUserDetail> details) {
        if (details == null || details.isEmpty()) {
            return;
        }
        saveBatch(details);
    }

    @Override
    public List<LdapSyncUserDetail> getByRecordIdAndAction(Integer recordId, Integer action) {
        QueryWrapper<LdapSyncUserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_id", recordId);
        if (action > 0) {
            queryWrapper.eq("action", action);
        }
        queryWrapper.orderByDesc("id");
        return list(queryWrapper);
    }
}
