package xyz.playedu.api.controller.backend;

import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.event.CourseHourCreatedEvent;
import xyz.playedu.api.event.CourseHourDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseHourRequest;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.SelectOption;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 17:50
 */
@RestController
@RequestMapping("/backend/v1/course/{courseId}/hour")
public class CourseHourController {

    @Autowired
    private CourseHourService hourService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index(@PathVariable(name = "courseId") Integer courseId) {
        List<CourseHour> hours = hourService.getHoursByCourseId(courseId);
        return JsonResponse.data(hours);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/create")
    public JsonResponse create(@PathVariable(name = "courseId") Integer courseId) {
        // 课时类型
        List<SelectOption<String>> typeItems = new ArrayList<>();
        for (int i = 0; i < BackendConstant.COURSE_HOUR_TYPE_WHITELIST.length; i++) {
            SelectOption<String> tmpTypeItem = new SelectOption<>();
            tmpTypeItem.setKey(BackendConstant.COURSE_HOUR_TYPE_WHITELIST[i]);
            tmpTypeItem.setValue(BackendConstant.COURSE_HOUR_TYPE_WHITELIST_TEXT[i]);

            typeItems.add(tmpTypeItem);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("types", typeItems);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    public JsonResponse store(@PathVariable(name = "courseId") Integer courseId, @RequestBody @Validated CourseHourRequest req) {
        if (!Arrays.asList(BackendConstant.COURSE_HOUR_TYPE_WHITELIST).contains(req.getType())) {
            return JsonResponse.error("课时类型不支持");
        }
        CourseHour courseHour = hourService.create(courseId, req.getChapterId(), req.getTitle(), req.getType(), req.getDuration(), req.getPublishedAt());
        ctx.publishEvent(new CourseHourCreatedEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), courseHour.getCourseId(), courseHour.getChapterId(), courseHour.getId(), new Date()));
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        return JsonResponse.data(courseHour);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseHourRequest req) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        hourService.update(courseHour, req.getChapterId(), req.getTitle(), req.getDuration(), req.getPublishedAt());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        hourService.removeById(courseHour.getId());
        ctx.publishEvent(new CourseHourDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), courseHour.getCourseId(), courseHour.getChapterId(), courseHour.getId(), new Date()));
        return JsonResponse.success();
    }

}
