package xyz.playedu.api.controller.backend;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.event.CourseHourCreatedEvent;
import xyz.playedu.api.event.CourseHourDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.request.backend.CourseHourRequest;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.types.JsonResponse;

import java.util.Date;
import java.util.List;

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

    @GetMapping("/create")
    public JsonResponse create(@PathVariable(name = "courseId") Integer courseId) {
        return JsonResponse.data(null);
    }

    @PostMapping("/create")
    public JsonResponse store(@PathVariable(name = "courseId") Integer courseId, @RequestBody @Validated CourseHourRequest req) {
        CourseHour courseHour = hourService.create(courseId, req.getChapterId(), req.getTitle(), req.getType(), req.getDuration(), req.getPublishedAt());
        ctx.publishEvent(new CourseHourCreatedEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), courseHour.getCourseId(), courseHour.getChapterId(), courseHour.getId(), new Date()));
        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        return JsonResponse.data(courseHour);
    }

    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseHourRequest req) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        hourService.update(courseHour, req.getChapterId(), req.getTitle(), req.getDuration(), req.getPublishedAt());
        return JsonResponse.success();
    }

    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        hourService.removeById(courseHour.getId());
        ctx.publishEvent(new CourseHourDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), courseHour.getCourseId(), courseHour.getChapterId(), courseHour.getId(), new Date()));
        return JsonResponse.success();
    }

}
