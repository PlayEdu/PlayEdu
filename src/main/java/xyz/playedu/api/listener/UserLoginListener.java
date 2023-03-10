package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.playedu.api.caches.UserLoginCache;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.service.UserLoginRecordService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.util.IpUtil;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/10 13:45
 */
@Component
@Slf4j
public class UserLoginListener {

    @Autowired
    private UserLoginRecordService loginRecordService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserLoginCache userLoginCache;

    @EventListener
    @Async
    public void updateLoginInfo(UserLoginEvent event) throws JwtLogoutException {
        String ipArea = IpUtil.getRealAddressByIP(event.getIp());
        JWTPayload payload = jwtService.parse(event.getToken(), SystemConstant.JWT_PRV_USER);
        loginRecordService.store(
                event.getUserId(),
                payload.getJti(),
                payload.getExp(),
                event.getIp(),
                ipArea,
                event.getUserAgent().getBrowser().toString(),
                event.getUserAgent().getVersion(),
                event.getUserAgent().getOs().toString()
        );
    }

    @EventListener
    public void writeCache(UserLoginEvent event) {
        userLoginCache.put(event.getEmail());
    }

}
