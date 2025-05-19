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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import org.springframework.stereotype.Service;
import xyz.playedu.common.domain.LdapSyncRecord;
import xyz.playedu.common.mapper.LdapSyncRecordMapper;
import xyz.playedu.common.service.LdapSyncRecordService;
import xyz.playedu.common.types.paginate.PaginationResult;

@Service
public class LdapSyncRecordServiceImpl extends ServiceImpl<LdapSyncRecordMapper, LdapSyncRecord>
        implements LdapSyncRecordService {

    @Override
    public LdapSyncRecord create(Integer adminId) {
        LdapSyncRecord record = new LdapSyncRecord();
        record.setAdminId(adminId);
        record.setStatus(0); // 进行中
        record.setTotalDepartmentCount(0);
        record.setCreatedDepartmentCount(0);
        record.setUpdatedDepartmentCount(0);
        record.setDeletedDepartmentCount(0);
        record.setTotalUserCount(0);
        record.setCreatedUserCount(0);
        record.setUpdatedUserCount(0);
        record.setDeletedUserCount(0);
        record.setBannedUserCount(0);
        record.setCreatedAt(new Date());
        record.setUpdatedAt(new Date());

        save(record);
        return record;
    }

    @Override
    public void updateSyncResult(
            Integer id,
            Integer status,
            String s3FilePath,
            Integer totalDepartmentCount,
            Integer createdDepartmentCount,
            Integer updatedDepartmentCount,
            Integer deletedDepartmentCount,
            Integer totalUserCount,
            Integer createdUserCount,
            Integer updatedUserCount,
            Integer deletedUserCount,
            Integer bannedUserCount) {

        LdapSyncRecord record = new LdapSyncRecord();
        record.setId(id);
        record.setStatus(status);
        record.setS3FilePath(s3FilePath);
        record.setTotalDepartmentCount(totalDepartmentCount);
        record.setCreatedDepartmentCount(createdDepartmentCount);
        record.setUpdatedDepartmentCount(updatedDepartmentCount);
        record.setDeletedDepartmentCount(deletedDepartmentCount);
        record.setTotalUserCount(totalUserCount);
        record.setCreatedUserCount(createdUserCount);
        record.setUpdatedUserCount(updatedUserCount);
        record.setDeletedUserCount(deletedUserCount);
        record.setBannedUserCount(bannedUserCount);
        record.setUpdatedAt(new Date());

        updateById(record);
    }

    @Override
    public void updateSyncFailed(Integer id, String errorMessage) {
        LdapSyncRecord record = new LdapSyncRecord();
        record.setId(id);
        record.setStatus(2); // 失败
        record.setErrorMessage(errorMessage);
        record.setUpdatedAt(new Date());

        updateById(record);
    }

    @Override
    public PaginationResult<LdapSyncRecord> paginate(Integer page, Integer size) {
        Page<LdapSyncRecord> pageObj = new Page<>(page, size);
        QueryWrapper<LdapSyncRecord> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");

        IPage<LdapSyncRecord> iPage = page(pageObj, wrapper);

        PaginationResult<LdapSyncRecord> result = new PaginationResult<>();
        result.setData(iPage.getRecords());
        result.setTotal(iPage.getTotal());

        return result;
    }

    @Override
    public boolean hasSyncInProgress() {
        QueryWrapper<LdapSyncRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        return count(wrapper) > 0;
    }

    @Override
    public LdapSyncRecord getLatestRecord() {
        QueryWrapper<LdapSyncRecord> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 1");
        return getOne(wrapper);
    }
}
