package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBContext;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.event.ResourceCategoryDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.ResourceCategoryRequest;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.types.JsonResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 09:46
 */
@RestController
@RequestMapping("/backend/v1/resource-category")
public class ResourceCategoryController {

    @Autowired
    private ResourceCategoryService categoryService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index() {
        Map<Integer, List<ResourceCategory>> categories = categoryService.all().stream().collect(Collectors.groupingBy(ResourceCategory::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categories);

        return JsonResponse.data(data);
    }

    @GetMapping("/categories")
    public JsonResponse index(@RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<ResourceCategory> categories = categoryService.listByParentId(parentId);
        return JsonResponse.data(categories);
    }

    @GetMapping("/create")
    public JsonResponse create() {
        Map<Integer, List<ResourceCategory>> categories = categoryService.all().stream().collect(Collectors.groupingBy(ResourceCategory::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categories);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest req) throws NotFoundException {
        categoryService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody ResourceCategoryRequest req) throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        categoryService.update(category, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        categoryService.deleteById(category.getId());
        ctx.publishEvent(new ResourceCategoryDestroyEvent(this, PlayEduBContext.getAdminUserID(), category.getId(), new Date()));
        return JsonResponse.success();
    }

}
