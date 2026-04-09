package xyz.playedu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.course.domain.Exam;
import xyz.playedu.course.domain.ExamQuestion;
import xyz.playedu.course.domain.UserExamRecord;

import java.util.List;
import java.util.Map;

public interface ExamService extends IService<Exam> {

    Exam create(String title, String description, Integer duration, Integer passScore, 
               Integer totalScore, Integer isShow, Integer isRequired, 
               Integer[] categoryIds, Integer[] depIds, Integer adminId);

    void update(Exam exam, String title, String description, Integer duration, 
                Integer passScore, Integer totalScore, Integer isShow, 
                Integer isRequired, Integer[] categoryIds, Integer[] depIds);

    Exam findOrFail(Integer id);

    List<Exam> getOpenExamsAndShow(Integer limit);

    List<Exam> getDepExamsAndShow(List<Integer> depIds);

    List<ExamQuestion> getQuestionsByExamId(Integer examId);

    void addQuestion(Integer examId, Integer type, String content, 
                    String options, String answer, Integer score, Integer sort);

    void updateQuestion(Integer questionId, Integer type, String content, 
                       String options, String answer, Integer score, Integer sort);

    void deleteQuestion(Integer questionId);

    UserExamRecord submitExam(Integer userId, Integer examId, Integer usedTime, 
                             Map<Integer, String> answers);

    List<UserExamRecord> getUserExamRecords(Integer userId);

    UserExamRecord getLatestUserExamRecord(Integer userId, Integer examId);

    List<Exam> getRequiredExamsForUser(Integer userId);
}
