package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.CourseCategory;
import xyz.playedu.api.event.CourseCategoryDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseCategoryRequest;
import xyz.playedu.api.service.CourseCategoryService;
import xyz.playedu.api.types.JsonResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 13:57
 */
@RestController
@RequestMapping("/backend/v1/course-category")
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService categoryService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index() {
        Map<Integer, List<CourseCategory>> categories = categoryService.all().stream().collect(Collectors.groupingBy(CourseCategory::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categories);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_CATEGORY)
    @GetMapping("/create")
    public JsonResponse create(@RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<CourseCategory> data = categoryService.listByParentId(parentId);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_CATEGORY)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated CourseCategoryRequest req) throws NotFoundException {
        categoryService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_CATEGORY)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        CourseCategory category = categoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_CATEGORY)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody CourseCategoryRequest req) throws NotFoundException {
        CourseCategory category = categoryService.findOrFail(id);
        categoryService.update(category, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_CATEGORY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        CourseCategory category = categoryService.findOrFail(id);
        categoryService.deleteById(category.getId());
        ctx.publishEvent(new CourseCategoryDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), category.getId(), new Date()));
        return JsonResponse.success();
    }

}