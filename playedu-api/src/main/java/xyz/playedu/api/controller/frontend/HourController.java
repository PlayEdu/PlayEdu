/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.controller.frontend;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.event.UserCourseHourFinishedEvent;
import xyz.playedu.api.event.UserLearnCourseUpdateEvent;
import xyz.playedu.api.request.frontend.CourseHourRecordRequest;
import xyz.playedu.common.context.FCtx;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.RedisDistributedLock;
import xyz.playedu.course.bus.UserBus;
import xyz.playedu.course.caches.CourseCache;
import xyz.playedu.course.caches.UserCanSeeCourseCache;
import xyz.playedu.course.caches.UserLastLearnTimeCache;
import xyz.playedu.course.domain.Course;
import xyz.playedu.course.domain.CourseHour;
import xyz.playedu.course.domain.UserCourseHourRecord;
import xyz.playedu.course.service.CourseHourService;
import xyz.playedu.course.service.CourseService;
import xyz.playedu.course.service.UserCourseHourRecordService;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.service.ResourceService;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 14:59
 */
@RestController
@RequestMapping("/api/v1/course/{courseId}/hour")
public class HourController {

    @Autowired private CourseService courseService;

    @Autowired private CourseHourService hourService;

    @Autowired private ResourceService resourceService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserBus userBus;

    // ------- CACHE ----------
    @Autowired private UserCanSeeCourseCache userCanSeeCourseCache;
    @Autowired private CourseCache courseCache;

    @Autowired private RedisDistributedLock redisDistributedLock;

    @Autowired private UserLastLearnTimeCache userLastLearnTimeCache;

    @Autowired private ApplicationContext ctx;

    @GetMapping("/{id}")
    @SneakyThrows
    public JsonResponse detail(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id) {
        Course course = courseService.findOrFail(courseId);
        CourseHour courseHour = hourService.findOrFail(id, courseId);

        UserCourseHourRecord userCourseHourRecord = null;
        if (FCtx.getId() != null && FCtx.getId() > 0) {
            // 学员已登录
            userCourseHourRecord = userCourseHourRecordService.find(FCtx.getId(), courseId, id);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);
        data.put("hour", courseHour);
        data.put("user_hour_record", userCourseHourRecord);

        return JsonResponse.data(data);
    }

    @GetMapping("/{id}/play")
    @SneakyThrows
    public JsonResponse play(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id) {
        Course course = courseCache.findOrFail(courseId);
        userCanSeeCourseCache.check(FCtx.getUser(), course, true);
        CourseHour hour = hourService.findOrFail(id, courseId);
        Resource resource = resourceService.findOrFail(hour.getRid());

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", resource.getUrl()); // 视频播放地址
        data.put("extension", resource.getExtension()); // 视频格式
        data.put("duration", resourceService.duration(resource.getId())); // 视频时长

        return JsonResponse.data(data);
    }

    @PostMapping("/{id}/record")
    @SneakyThrows
    public JsonResponse record(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseHourRecordRequest req) {
        Integer duration = req.getDuration();
        if (duration <= 0) {
            return JsonResponse.error("duration参数错误");
        }

        Course course = courseCache.findOrFail(courseId);
        CourseHour hour = hourService.findOrFail(id, courseId);
        userCanSeeCourseCache.check(FCtx.getUser(), course, true);

        // 获取锁
        String lockKey = String.format("record:%d", FCtx.getId());
        boolean tryLock = redisDistributedLock.tryLock(lockKey, 5, TimeUnit.SECONDS);
        if (!tryLock) {
            return JsonResponse.error("请稍后再试");
        }

        try {
            boolean isFinished =
                    userCourseHourRecordService.storeOrUpdate(
                            FCtx.getId(),
                            course.getId(),
                            hour.getId(),
                            duration,
                            hour.getDuration());
            if (isFinished) {
                ctx.publishEvent(
                        new UserCourseHourFinishedEvent(
                                this, FCtx.getId(), courseId, hour.getId()));
            }
        } finally {
            // 此处未考虑上面代码执行失败释放锁
            redisDistributedLock.releaseLock(lockKey);
        }

        return JsonResponse.success();
    }

    @PostMapping("/{id}/ping")
    @SneakyThrows
    public JsonResponse ping(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id) {
        Course course = courseCache.findOrFail(courseId);
        CourseHour hour = hourService.findOrFail(id, courseId);
        userCanSeeCourseCache.check(FCtx.getUser(), course, true);

        // 获取锁
        String lockKey = String.format("ping:%d", FCtx.getId());
        boolean tryLock = redisDistributedLock.tryLock(lockKey, 5, TimeUnit.SECONDS);
        if (!tryLock) {
            return JsonResponse.error("请稍后再试");
        }

        try {
            Long curTime = System.currentTimeMillis();

            // 最近一次学习时间
            Long lastTime = userLastLearnTimeCache.get(FCtx.getId());
            // 最大周期为10s+0.5s的网络延迟
            if (lastTime == null || curTime - lastTime > 10500) {
                lastTime = curTime - 10000;
            }

            userLastLearnTimeCache.put(FCtx.getId(), curTime);

            ctx.publishEvent(
                    new UserLearnCourseUpdateEvent(
                            this, FCtx.getId(), course.getId(), hour.getId(), lastTime, curTime));
        } finally {
            // 此处未考虑上面代码执行失败释放锁
            redisDistributedLock.releaseLock(lockKey);
        }

        return JsonResponse.success();
    }
}
