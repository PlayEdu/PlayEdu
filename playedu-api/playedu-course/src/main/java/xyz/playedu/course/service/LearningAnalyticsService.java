package xyz.playedu.course.service;

import xyz.playedu.course.domain.UserLearnDurationStats;

import java.util.List;
import java.util.Map;

public interface LearningAnalyticsService {

    // 个人学习统计
    Map<String, Object> getUserLearningStats(Integer userId);

    // 部门学习统计
    Map<String, Object> getDepartmentLearningStats(Integer depId);

    // 企业学习统计
    Map<String, Object> getEnterpriseLearningStats();

    // 课程学习统计
    Map<String, Object> getCourseLearningStats(Integer courseId);

    // 学习行为分析
    Map<String, Object> getLearningBehaviorAnalysis(Integer userId);

    // 学习趋势分析
    List<Map<String, Object>> getLearningTrendAnalysis(Integer days);

    // 部门学习排名
    List<Map<String, Object>> getDepartmentLearningRanking();

    // 课程学习排名
    List<Map<String, Object>> getCourseLearningRanking();

    // 导出学习数据
    byte[] exportLearningData(String type, Map<String, Object> params);
}
