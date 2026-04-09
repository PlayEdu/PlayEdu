package xyz.playedu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.course.domain.LearningPath;
import xyz.playedu.course.domain.LearningPathNode;
import xyz.playedu.course.domain.UserLearningPathRecord;

import java.util.List;

public interface LearningPathService extends IService<LearningPath> {

    LearningPath create(String title, String description, Integer isShow, 
                       Integer isRequired, Integer[] depIds, Integer adminId);

    void update(LearningPath path, String title, String description, 
                Integer isShow, Integer isRequired, Integer[] depIds);

    LearningPath findOrFail(Integer id);

    List<LearningPath> getOpenPathsAndShow(Integer limit);

    List<LearningPath> getDepPathsAndShow(List<Integer> depIds);

    List<LearningPathNode> getNodesByPathId(Integer pathId);

    void addNode(Integer pathId, Integer type, Integer targetId, Integer sort);

    void updateNode(Integer nodeId, Integer type, Integer targetId, Integer sort);

    void deleteNode(Integer nodeId);

    UserLearningPathRecord getUserPathRecord(Integer userId, Integer pathId);

    void updateUserPathProgress(Integer userId, Integer pathId);

    List<UserLearningPathRecord> getUserPathRecords(Integer userId);

    List<LearningPath> getRequiredPathsForUser(Integer userId);
}
