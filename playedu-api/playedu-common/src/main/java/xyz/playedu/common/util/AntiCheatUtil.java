package xyz.playedu.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AntiCheatUtil {

    // 存储用户学习行为数据
    private static final Map<String, UserLearningBehavior> userLearningBehaviors = new ConcurrentHashMap<>();

    /**
     * 记录用户学习行为
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param hourId 课时ID
     * @param currentTime 当前观看时间（秒）
     * @param totalDuration 总时长（秒）
     * @return 是否检测到作弊行为
     */
    public static boolean recordLearningBehavior(Integer userId, Integer courseId, Integer hourId, 
                                               int currentTime, int totalDuration) {
        String key = userId + "_" + courseId + "_" + hourId;
        UserLearningBehavior behavior = userLearningBehaviors.get(key);
        
        if (behavior == null) {
            behavior = new UserLearningBehavior();
            behavior.setLastTime(currentTime);
            behavior.setStartTime(System.currentTimeMillis());
            userLearningBehaviors.put(key, behavior);
            return false;
        }

        // 检测快进行为
        if (currentTime - behavior.getLastTime() > 10) { // 超过10秒的跳跃视为快进
            behavior.setCheatDetected(true);
            return true;
        }

        // 检测挂机行为（长时间没有进度变化）
        long currentTimestamp = System.currentTimeMillis();
        if (currentTime == behavior.getLastTime() && 
            currentTimestamp - behavior.getLastActivityTime() > 300000) { // 5分钟无变化视为挂机
            behavior.setCheatDetected(true);
            return true;
        }

        // 更新行为数据
        behavior.setLastTime(currentTime);
        behavior.setLastActivityTime(currentTimestamp);
        
        // 检测学习完成时的异常行为
        if (currentTime >= totalDuration - 1) {
            long learningDuration = currentTimestamp - behavior.getStartTime();
            long expectedDuration = totalDuration * 1000 * 0.8; // 至少需要80%的时间
            if (learningDuration < expectedDuration) {
                behavior.setCheatDetected(true);
                return true;
            }
        }

        return false;
    }

    /**
     * 获取用户学习行为
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param hourId 课时ID
     * @return 用户学习行为
     */
    public static UserLearningBehavior getUserLearningBehavior(Integer userId, Integer courseId, Integer hourId) {
        String key = userId + "_" + courseId + "_" + hourId;
        return userLearningBehaviors.get(key);
    }

    /**
     * 清除用户学习行为
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param hourId 课时ID
     */
    public static void clearUserLearningBehavior(Integer userId, Integer courseId, Integer hourId) {
        String key = userId + "_" + courseId + "_" + hourId;
        userLearningBehaviors.remove(key);
    }

    // 用户学习行为类
    public static class UserLearningBehavior {
        private int lastTime; // 上次观看时间
        private long startTime; // 开始学习时间
        private long lastActivityTime; // 上次活动时间
        private boolean cheatDetected; // 是否检测到作弊

        public UserLearningBehavior() {
            this.lastActivityTime = System.currentTimeMillis();
        }

        public int getLastTime() {
            return lastTime;
        }

        public void setLastTime(int lastTime) {
            this.lastTime = lastTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getLastActivityTime() {
            return lastActivityTime;
        }

        public void setLastActivityTime(long lastActivityTime) {
            this.lastActivityTime = lastActivityTime;
        }

        public boolean isCheatDetected() {
            return cheatDetected;
        }

        public void setCheatDetected(boolean cheatDetected) {
            this.cheatDetected = cheatDetected;
        }
    }
}
