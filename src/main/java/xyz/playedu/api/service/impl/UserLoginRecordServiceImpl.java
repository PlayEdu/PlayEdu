/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.UserLoginRecord;
import xyz.playedu.api.mapper.UserLoginRecordMapper;
import xyz.playedu.api.service.UserLoginRecordService;

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
    public void saveIpArea(Integer id, String area) {
        UserLoginRecord record = new UserLoginRecord();
        record.setId(id);
        record.setIpArea(area);
        updateById(record);
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
}
