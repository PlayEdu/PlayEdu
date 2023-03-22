package xyz.playedu.api.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.bus.UserBus;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.util.RedisUtil;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 15:20
 */
@Component
public class UserCanSeeCourseCache {

    @Autowired
    private UserBus userBus;

    private final static String keyTemplate = "c:%d-u:%d";

    private final static int expire = 3600;//s

    public boolean check(User user, Course course, boolean isThrow) throws ServiceException {
        boolean result;
        if (RedisUtil.exists(key(user, course))) {
            String cacheResult = (String) RedisUtil.get(key(user, course));
            result = "1".equals(cacheResult);
        } else {
            result = userBus.canSeeCourse(user, course);
            put(user, course, result);
        }
        if (!result && isThrow) {
            throw new ServiceException("无权限观看");
        }
        return result;
    }

    public void put(User user, Course course, boolean result) {
        RedisUtil.set(key(user, course), result ? "1" : "0", expire);
    }

    private String key(User user, Course course) {
        return String.format(keyTemplate, course.getId(), user.getId());
    }

}
