package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.event.CourseDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseRequest;
import xyz.playedu.api.service.CourseCategoryService;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 14:16
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseCategoryService categoryService;//课程分类

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String title = MapUtils.getString(params, "title");
        String depIds = MapUtils.getString(params, "dep_ids");
        String categoryIds = MapUtils.getString(params, "category_ids");

        CoursePaginateFiler filter = new CoursePaginateFiler();
        filter.setTitle(title);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);
        if (depIds != null && depIds.length() > 0) {
            filter.setDepIds(Arrays.stream(depIds.split(",")).map(Integer::valueOf).toArray(Integer[]::new));
        }
        if (categoryIds != null && categoryIds.length() > 0) {
            filter.setCategoryIds(Arrays.stream(categoryIds.split(",")).map(Integer::valueOf).toArray(Integer[]::new));
        }

        log.info("filter:" + filter);

        PaginationResult<Course> result = courseService.paginate(page, size, filter);
        return JsonResponse.data(result);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/create")
    public JsonResponse create() {
        Map<Integer, List<CourseCategory>> categories = categoryService.all().stream().collect(Collectors.groupingBy(CourseCategory::getParentId));
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Transactional
    public JsonResponse store(@RequestBody @Validated CourseRequest req) {
        courseService.createWithCategoryIdsAndDepIds(req.getTitle(), req.getThumb(), req.getIsShow(), req.getCategoryIds(), req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Course course = courseService.findOrFail(id);
        List<Integer> depIds = courseService.getDepIdsByCourseId(course.getId());
        List<Integer> categoryIds = courseService.getCategoryIdsByCourseId(course.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);
        data.put("dep_ids", depIds);
        data.put("category_ids", categoryIds);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Transactional
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated CourseRequest req) throws NotFoundException {
        Course course = courseService.findOrFail(id);
        courseService.updateWithCategoryIdsAndDepIds(course, req.getTitle(), req.getThumb(), req.getIsShow(), req.getCategoryIds(), req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        courseService.removeById(id);
        ctx.publishEvent(new CourseDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), id, new Date()));
        return JsonResponse.success();
    }

}
