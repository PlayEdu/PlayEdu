/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import xyz.playedu.api.types.mapper.UserCourseHourRecordCountMapper;
import xyz.playedu.api.types.response.UserLatestLearn;
import xyz.playedu.api.util.PrivacyUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/13 09:21
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired private UserService userService;

    @Autowired private DepartmentService departmentService;

    @Autowired private CourseService courseService;

    @Autowired private CourseHourService hourService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @Autowired private UploadService uploadService;

    @GetMapping("/detail")
    public JsonResponse detail() {
        User user = FCtx.getUser();
        List<Department> departments =
                departmentService.listByIds(userService.getDepIdsByUserId(user.getId()));

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
        UserUploadImageLog log =
                uploadService.userAvatar(
                        FCtx.getId(),
                        file,
                        FrontendConstant.USER_UPLOAD_IMAGE_TYPE_AVATAR,
                        FrontendConstant.USER_UPLOAD_IMAGE_SCENE_AVATAR);
        userService.changeAvatar(FCtx.getId(), log.getUrl());
        return JsonResponse.success();
    }

    @PutMapping("/password")
    public JsonResponse changePassword(@RequestBody @Validated ChangePasswordRequest req)
            throws ServiceException {
        userService.passwordChange(FCtx.getUser(), req.getOldPassword(), req.getNewPassword());
        return JsonResponse.success();
    }

    @GetMapping("/courses")
    public JsonResponse courses(@RequestParam HashMap<String, Object> params) {
        Integer depId = MapUtils.getInteger(params, "dep_id");
        if (depId == null || depId == 0) {
            return JsonResponse.error("请选择部门");
        }

        Integer categoryId = MapUtils.getInteger(params, "category_id");

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
        List<Course> depCourses =
                courseService.getDepCoursesAndShow(
                        new ArrayList<>() {
                            {
                                add(depId);
                            }
                        },
                        categoryId);
        // 全部部门课
        List<Course> openCourses = courseService.getOpenCoursesAndShow(500, categoryId);
        // 汇总到一个list中
        if (depCourses != null && depCourses.size() > 0) {
            courses.addAll(depCourses);
        }
        if (openCourses != null && openCourses.size() > 0) {
            courses.addAll(openCourses);
        }
        // 对结果进行排序->按照课程id倒序
        if (courses.size() > 0) {
            courses =
                    courses.stream()
                            .sorted(Comparator.comparing(Course::getId).reversed())
                            .toList();
        }

        data.put("courses", courses);

        List<Integer> courseIds = courses.stream().map(Course::getId).toList();

        // -------- 读取学习进度 ----------
        Map<Integer, UserCourseRecord> learnCourseRecords = new HashMap<>();
        if (courses.size() > 0) {
            learnCourseRecords =
                    userCourseRecordService.chunk(FCtx.getId(), courseIds).stream()
                            .collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e));
        }
        data.put("learn_course_records", learnCourseRecords);

        int requiredCourseCount = 0;
        int nunRequiredCourseCount = 0;
        int requiredFinishedCourseCount = 0; // 已完成必修课
        int nunRequiredFinishedCourseCount = 0; // 已完成选修课
        int requiredHourCount = 0; // 必修课时
        int nunRequiredHourCount = 0; // 选修课时
        int requiredFinishedHourCount = 0; // 已完成必修课时
        int nunRequiredFinishedHourCount = 0; // 已完成选修课时
        Long todayLearnDuration =
                userLearnDurationStatsService.todayUserDuration(FCtx.getId()); // 今日学习时长
        Long learnDuration = userLearnDurationStatsService.userDuration(FCtx.getId()); // 学习总时长

        // -------- 学习数据统计 ----------
        if (courses.size() > 0) {
            for (Course courseItem : courses) {
                if (courseItem.getIsRequired() == 1) {
                    requiredHourCount += courseItem.getClassHour();
                    requiredCourseCount += 1;
                } else {
                    nunRequiredHourCount += courseItem.getClassHour();
                    nunRequiredCourseCount += 1;
                }
                UserCourseRecord learnRecord = learnCourseRecords.get(courseItem.getId());
                if (learnRecord == null) {
                    continue;
                }
                if (courseItem.getIsRequired() == 1) {
                    requiredFinishedHourCount += learnRecord.getFinishedCount();
                    if (learnRecord.getIsFinished() == 1) {
                        requiredFinishedCourseCount++;
                    }
                } else {
                    nunRequiredFinishedHourCount += learnRecord.getFinishedCount();
                    if (learnRecord.getIsFinished() == 1) {
                        nunRequiredFinishedCourseCount++;
                    }
                }
            }
        }
        HashMap<String, Object> stats = new HashMap<>();
        stats.put("required_course_count", requiredCourseCount); // 必修课数量
        stats.put("nun_required_course_count", nunRequiredCourseCount); // 选修课数量
        stats.put("required_finished_course_count", requiredFinishedCourseCount); // 必修已完成线上课数
        stats.put(
                "nun_required_finished_course_count", nunRequiredFinishedCourseCount); // 选修已完成线上课数
        stats.put("required_hour_count", requiredHourCount); // 必修课时总数
        stats.put("nun_required_hour_count", nunRequiredHourCount); // 选修课时总数
        stats.put("required_finished_hour_count", requiredFinishedHourCount); // 必修已完成课时数
        stats.put("nun_required_finished_hour_count", nunRequiredFinishedHourCount); // 选修已完成课时数
        stats.put("today_learn_duration", todayLearnDuration); // 今日学习时长[单位:毫秒]
        stats.put("learn_duration", learnDuration); // 学习总时长[单位:毫秒]
        data.put("stats", stats);

        // 当前学员每个线上课的学习课时数量(只要学习了就算，不一定需要完成)
        data.put(
                "user_course_hour_count",
                userCourseHourRecordService
                        .getUserCourseHourCount(FCtx.getId(), courseIds, null)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        UserCourseHourRecordCountMapper::getCourseId,
                                        UserCourseHourRecordCountMapper::getTotal)));

        return JsonResponse.data(data);
    }

    @GetMapping("/latest-learn")
    public JsonResponse latestLearn() {
        // 读取当前学员最近100条学习的线上课
        List<UserCourseHourRecord> userCourseHourRecords =
                userCourseHourRecordService.getLatestCourseIds(FCtx.getId(), 100);
        if (userCourseHourRecords == null || userCourseHourRecords.size() == 0) {
            return JsonResponse.data(new ArrayList<>());
        }

        List<Integer> courseIds =
                userCourseHourRecords.stream().map(UserCourseHourRecord::getCourseId).toList();
        List<Integer> hourIds =
                userCourseHourRecords.stream().map(UserCourseHourRecord::getHourId).toList();
        Map<Integer, UserCourseHourRecord> hour2Record =
                userCourseHourRecords.stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getHourId, e -> e));
        Map<Integer, Integer> course2hour =
                userCourseHourRecords.stream()
                        .collect(
                                Collectors.toMap(
                                        UserCourseHourRecord::getCourseId,
                                        UserCourseHourRecord::getHourId));

        // 线上课
        Map<Integer, Course> courses =
                courseService
                        .chunks(
                                courseIds,
                                new ArrayList<>() {
                                    {
                                        add("id");
                                        add("title");
                                        add("thumb");
                                        add("short_desc");
                                        add("class_hour");
                                        add("is_required");
                                    }
                                })
                        .stream()
                        .collect(Collectors.toMap(Course::getId, e -> e));

        // 线上课课时
        Map<Integer, CourseHour> hours =
                hourService.chunk(hourIds).stream()
                        .collect(Collectors.toMap(CourseHour::getId, e -> e));

        // 获取学员的线上课进度
        Map<Integer, UserCourseRecord> records =
                userCourseRecordService.chunk(FCtx.getId(), courseIds).stream()
                        .collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e));
        List<UserLatestLearn> userLatestLearns = new ArrayList<>();
        for (Integer courseId : courseIds) {
            UserCourseRecord record = records.get(courseId); // 线上课学习进度
            Course tmpCourse = courses.get(courseId); // 线上课
            Integer tmpHourId = course2hour.get(courseId); // 最近学习的课时id
            UserCourseHourRecord tmpUserCourseHourRecord = hour2Record.get(tmpHourId); // 课时学习进度
            CourseHour tmpHour = hours.get(tmpHourId); // 课时

            userLatestLearns.add(
                    new UserLatestLearn() {
                        {
                            setCourse(tmpCourse);
                            setUserCourseRecord(record);
                            setHourRecord(tmpUserCourseHourRecord);
                            setLastLearnHour(tmpHour);
                        }
                    });
        }

        return JsonResponse.data(userLatestLearns);
    }
}
