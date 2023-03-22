package xyz.playedu.api.controller.frontend;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.FCtx;
import xyz.playedu.api.bus.UserBus;
import xyz.playedu.api.caches.CourseCache;
import xyz.playedu.api.caches.UserCanSeeCourseCache;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.request.frontend.CourseHourRecordRequest;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.UserCourseHourRecordService;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 14:59
 */
@RestController
@RequestMapping("/api/v1/course/{courseId}/hour")
public class HourController {

    @Autowired
    private CourseHourService hourService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired
    private UserBus userBus;


    // ------- CACHE ----------
    @Autowired
    private UserCanSeeCourseCache userCanSeeCourseCache;
    @Autowired
    private CourseCache courseCache;

    @GetMapping("/{id}/play")
    @SneakyThrows
    public JsonResponse play(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) {
        Course course = courseCache.findOrFail(courseId);
        userCanSeeCourseCache.check(FCtx.getUser(), course, true);
        CourseHour hour = hourService.findOrFail(id, courseId);
        Resource resource = resourceService.findOrFail(hour.getRid());

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", resource.getUrl());//视频播放地址
        data.put("extension", resource.getExtension());//视频格式
        data.put("duration", resourceService.duration(resource.getId()));//视频时长

        return JsonResponse.data(data);
    }

    @PostMapping("/{id}/record")
    @SneakyThrows
    public JsonResponse record(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseHourRecordRequest req) {
        Integer duration = req.getDuration();
        if (duration <= 0) {
            return JsonResponse.error("duration参数错误");
        }
        User user = FCtx.getUser();
        // 线上课检测
        Course course = courseCache.findOrFail(courseId);
        // 权限校验
        userCanSeeCourseCache.check(user, course, true);
        // 课时检测
        CourseHour hour = hourService.findOrFail(id, courseId);

        userCourseHourRecordService.storeOrUpdate(user.getId(), course.getId(), hour.getId(), duration, hour.getDuration());

        return JsonResponse.success();
    }

    @PostMapping("/{id}/ping")
    @SneakyThrows
    public JsonResponse ping(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) {
        Course course = courseCache.findOrFail(courseId);
        CourseHour hour = hourService.findOrFail(id, courseId);
        userCanSeeCourseCache.check(FCtx.getUser(), course, true);
        userBus.userLearnDurationRecord(FCtx.getUser(), course, hour);
        return JsonResponse.success();
    }

}
