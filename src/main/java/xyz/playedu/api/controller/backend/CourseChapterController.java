package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.domain.CourseChapter;
import xyz.playedu.api.event.CourseChapterDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.request.backend.CourseChapterRequest;
import xyz.playedu.api.service.CourseChapterService;
import xyz.playedu.api.types.JsonResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 17:28
 */
@RestController
@RequestMapping("/backend/v1/course/{courseId}/chapter")
public class CourseChapterController {

    @Autowired
    private CourseChapterService chapterService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index(@PathVariable(name = "courseId") Integer courseId) {
        List<CourseChapter> chapters = chapterService.getChaptersByCourseId(courseId);
        return JsonResponse.data(chapters);
    }

    @GetMapping("/create")
    public JsonResponse create(@PathVariable(name = "courseId") Integer courseId) {
        List<CourseChapter> chapters = chapterService.getChaptersByCourseId(courseId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("chapters", chapters);
        return JsonResponse.data(data);
    }

    @PostMapping("/create")
    public JsonResponse store(@PathVariable(name = "courseId") Integer courseId, @RequestBody @Validated CourseChapterRequest req) {
        chapterService.create(courseId, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        return JsonResponse.data(chapter);
    }

    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseChapterRequest req) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        chapterService.update(chapter, req.getName(), req.getSort());
        return JsonResponse.data(chapter);
    }

    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        chapterService.removeById(chapter.getId());
        ctx.publishEvent(new CourseChapterDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), chapter.getCourseId(), new Date()));
        return JsonResponse.success();
    }
}
