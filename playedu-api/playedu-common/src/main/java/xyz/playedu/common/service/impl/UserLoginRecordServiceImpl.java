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

import xyz.playedu.common.domain.UserLoginRecord;
import xyz.playedu.common.mapper.UserLoginRecordMapper;
import xyz.playedu.common.service.UserLoginRecordService;

/**
 * @author tengteng
 * @description 针对表【user_login_records】的数据库操作Service实现
 * @createDate 2023-03-10 13:40:33
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord>
        implements UserLoginRecordService {
    @Override
    public UserLoginRecord store(
            Integer userId,
            String jti,
            Long expired,
            String ip,
            String ipArea,
            String browser,
            String browserVersion,
            String os) {
        UserLoginRecord record = new UserLoginRecord();
        record.setUserId(userId);
        record.setJti(jti);
        record.setExpired(expired);
        record.setIp(ip);
        record.setIpArea(ipArea);
        record.setBrowser(browser);
        record.setBrowserVersion(browserVersion);
        record.setOs(os);
        save(record);
        return record;
    }

    @Override
    public void logout(Integer userid, String jti) {
        UserLoginRecord record =
                getOne(
                        query().getWrapper()
                                .eq("user_id", userid)
                                .eq("jti", jti)
                                .eq("is_logout", 0));
        if (record == null) {
            return;
        }
        UserLoginRecord newRecord = new UserLoginRecord();
        newRecord.setId(record.getId());
        newRecord.setIsLogout(1);

        updateById(newRecord);
    }

    @Override
    public void remove(Integer userId) {
        remove(query().getWrapper().eq("user_id", userId));
    }
}
