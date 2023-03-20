package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBCtx;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.CourseChapter;
import xyz.playedu.api.event.CourseChapterDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseChapterRequest;
import xyz.playedu.api.request.backend.CourseChapterSortRequest;
import xyz.playedu.api.service.CourseChapterService;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.types.JsonResponse;

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
    private CourseHourService hourService;

    @Autowired
    private ApplicationContext ctx;

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    public JsonResponse store(@PathVariable(name = "courseId") Integer courseId, @RequestBody @Validated CourseChapterRequest req) {
        chapterService.create(courseId, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        return JsonResponse.data(chapter);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseChapterRequest req) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        chapterService.update(chapter, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "courseId") Integer courseId, @PathVariable(name = "id") Integer id) throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        if (hourService.getCountByChapterId(chapter.getId()) > 0) {
            return JsonResponse.error("当前章节下面存在课时无法删除");
        }
        chapterService.removeById(chapter.getId());
        ctx.publishEvent(new CourseChapterDestroyEvent(this, PlayEduBCtx.getAdminUserID(), chapter.getCourseId(), chapter.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    public JsonResponse updateSort(@PathVariable(name = "courseId") Integer courseId, @RequestBody @Validated CourseChapterSortRequest req) {
        chapterService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
