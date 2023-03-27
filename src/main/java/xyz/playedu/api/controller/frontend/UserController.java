package xyz.playedu.api.controller.frontend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.FCtx;
import xyz.playedu.api.constant.FrontendConstant;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.request.frontend.ChangePasswordRequest;
import xyz.playedu.api.service.*;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.response.UserLatestLearn;
import xyz.playedu.api.util.PrivacyUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:21
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseHourService hourService;

    @Autowired
    private UserCourseRecordService userCourseRecordService;

    @Autowired
    private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired
    private UserLearnDurationStatsService userLearnDurationStatsService;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/detail")
    public JsonResponse detail() {
        User user = FCtx.getUser();
        List<Department> departments = departmentService.listByIds(userService.getDepIdsByUserId(user.getId()));

        if (user.getIdCard() != null && user.getIdCard().length() > 0) {
            user.setIdCard(PrivacyUtil.hideIDCard(user.getIdCard()));
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @PutMapping("/avatar")
    public JsonResponse changeAvatar(MultipartFile file) {
        UserUploadImageLog log = uploadService.userAvatar(FCtx.getId(), file, FrontendConstant.USER_UPLOAD_IMAGE_TYPE_AVATAR, FrontendConstant.USER_UPLOAD_IMAGE_SCENE_AVATAR);
        userService.changeAvatar(FCtx.getId(), log.getUrl());
        return JsonResponse.success();
    }

    @PutMapping("/password")
    public JsonResponse changePassword(@RequestBody @Validated ChangePasswordRequest req) throws ServiceException {
        userService.passwordChange(FCtx.getUser(), req.getOldPassword(), req.getNewPassword());
        return JsonResponse.success();
    }

    @GetMapping("/courses")
    public JsonResponse courses(@RequestParam HashMap<String, Object> params) {
        Integer depId = MapUtils.getInteger(params, "dep_id");
        if (depId == null || depId == 0) {
            return JsonResponse.error("请选择部门");
        }

        List<Integer> userJoinDepIds = userService.getDepIdsByUserId(FCtx.getId());
        if (userJoinDepIds == null) {
            return JsonResponse.error("当前学员未加入任何部门");
        }
        if (!userJoinDepIds.contains(depId)) {
            return JsonResponse.error("当前学员未加入所选择部门");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("learn_course_records", new HashMap<>());

        // -------- 读取当前学员可以参加的课程 ----------
        List<Course> courses = new ArrayList<>();
        // 读取部门课
        List<Course> depCourses = courseService.getDepCoursesAndShow(new ArrayList<>() {{
            add(depId);
        }});
        // 全部部门课
        List<Course> openCourses = courseService.getOpenCoursesAndShow(500);
        // 汇总到一个list中
        if (depCourses != null && depCourses.size() > 0) {
            courses.addAll(depCourses);
        }
        if (openCourses != null && openCourses.size() > 0) {
            courses.addAll(openCourses);
        }
        // 对结果进行排序->按照课程id倒序
        if (courses.size() > 0) {
            courses = courses.stream().sorted(Comparator.comparing(Course::getId).reversed()).toList();
        }

        data.put("courses", courses);

        // -------- 读取学习进度 ----------
        Map<Integer, UserCourseRecord> learnCourseRecords = new HashMap<>();
        if (courses.size() > 0) {
            learnCourseRecords = userCourseRecordService.chunk(FCtx.getId(), courses.stream().map(Course::getId).toList()).stream().collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e));
        }
        data.put("learn_course_records", learnCourseRecords);

        Integer requiredHourCount = 0;//必修课时
        Integer nunRequiredHourCount = 0;//选修课时
        Integer requiredFinishedHourCount = 0;//已完成必修课时
        Integer nunRequiredFinishedHourCount = 0;//已完成选修课时
        Integer todayLearnDuration = userLearnDurationStatsService.todayUserDuration(FCtx.getId());//今日学习时长
        Integer learnDuration = userLearnDurationStatsService.userDuration(FCtx.getId());//学习总时长

        // -------- 学习数据统计 ----------
        if (courses.size() > 0) {
            for (Course courseItem : courses) {
                if (courseItem.getIsRequired() == 1) {
                    requiredHourCount += courseItem.getClassHour();
                } else {
                    nunRequiredHourCount += courseItem.getClassHour();
                }
                UserCourseRecord learnRecord = learnCourseRecords.get(courseItem.getId());
                if (learnRecord == null) {
                    continue;
                }
                if (courseItem.getIsRequired() == 1) {
                    requiredFinishedHourCount += learnRecord.getFinishedCount();
                } else {
                    nunRequiredFinishedHourCount += learnRecord.getFinishedCount();
                }
            }
        }
        HashMap<String, Integer> stats = new HashMap<>();
        stats.put("required_hour_count", requiredHourCount);
        stats.put("nun_required_hour_count", nunRequiredHourCount);
        stats.put("required_finished_hour_count", requiredFinishedHourCount);
        stats.put("nun_required_finished_hour_count", nunRequiredFinishedHourCount);
        stats.put("today_learn_duration", todayLearnDuration);
        stats.put("learn_duration", learnDuration);
        data.put("stats", stats);

        return JsonResponse.data(data);
    }

    @GetMapping("/latest-learn")
    public JsonResponse latestLearn() {
        // 读取当前学员最近100条学习的线上课
        List<UserCourseHourRecord> userCourseHourRecords = userCourseHourRecordService.getLatestCourseIds(FCtx.getId(), 100);
        if (userCourseHourRecords == null || userCourseHourRecords.size() == 0) {
            return JsonResponse.data(new ArrayList<>());
        }

        List<Integer> courseIds = userCourseHourRecords.stream().map(UserCourseHourRecord::getCourseId).toList();
        List<Integer> hourIds = userCourseHourRecords.stream().map(UserCourseHourRecord::getHourId).toList();
        Map<Integer, UserCourseHourRecord> hour2Record = userCourseHourRecords.stream().collect(Collectors.toMap(UserCourseHourRecord::getHourId, e -> e));
        Map<Integer, Integer> course2hour = userCourseHourRecords.stream().collect(Collectors.toMap(UserCourseHourRecord::getCourseId, UserCourseHourRecord::getHourId));

        // 线上课
        Map<Integer, Course> courses = courseService.chunks(courseIds, new ArrayList<>() {{
            add("id");
            add("title");
            add("thumb");
            add("short_desc");
            add("class_hour");
            add("is_required");
        }}).stream().collect(Collectors.toMap(Course::getId, e -> e));

        // 线上课课时
        Map<Integer, CourseHour> hours = hourService.chunk(hourIds).stream().collect(Collectors.toMap(CourseHour::getId, e -> e));

        // 获取学员的线上课进度
        Map<Integer, UserCourseRecord> records = userCourseRecordService.chunk(FCtx.getId(), courseIds).stream().collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e));
        List<UserLatestLearn> userLatestLearns = new ArrayList<>();
        for (Integer courseId : courseIds) {
            UserCourseRecord record = records.get(courseId);//线上课学习进度
            Course tmpCourse = courses.get(courseId);//线上课
            Integer tmpHourId = course2hour.get(courseId);//最近学习的课时id
            UserCourseHourRecord tmpUserCourseHourRecord = hour2Record.get(tmpHourId);//课时学习进度
            CourseHour tmpHour = hours.get(tmpHourId);//课时

            userLatestLearns.add(new UserLatestLearn() {{
                setCourse(tmpCourse);
                setUserCourseRecord(record);
                setHourRecord(tmpUserCourseHourRecord);
                setLastLearnHour(tmpHour);
            }});
        }

        return JsonResponse.data(userLatestLearns);
    }

}