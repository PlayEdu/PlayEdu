/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.api.config.PlayEduConfig;
import xyz.playedu.api.constant.SystemConstant;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/19 12:06
 */
@Component
public class AppBus {

    @Autowired private PlayEduConfig playEduConfig;

    public boolean isDev() {
        return !playEduConfig.getEnv().equals(SystemConstant.ENV_PROD);
    }
}
