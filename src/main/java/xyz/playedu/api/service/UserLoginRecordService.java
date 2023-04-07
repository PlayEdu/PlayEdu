/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.UserLoginRecord;

/**
 * @author tengteng
 * @description 针对表【user_login_records】的数据库操作Service
 * @createDate 2023-03-10 13:40:33
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {
    UserLoginRecord store(
            Integer userId,
            String jti,
            Long expired,
            String ip,
            String ipArea,
            String browser,
            String browserVersion,
            String os);

    void saveIpArea(Integer id, String area);

    void logout(Integer userid, String jti);
}
