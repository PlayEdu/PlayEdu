package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBackendThreadLocal;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.event.ResourceCategoryDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.ResourceCategoryRequest;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.types.JsonResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 09:46
 */
@RestController
@RequestMapping("/backend/v1/resource-category")
public class ResourceCategoryController {

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam(name = "type") String type) {
        List<ResourceCategory> categories = resourceCategoryService.getByType(type);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", categories);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/create")
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("types", BackendConstant.RESOURCE_EXT_WHITELIST);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest req) {
        if (!Arrays.asList(BackendConstant.RESOURCE_EXT_WHITELIST).contains(req.getType())) {
            return JsonResponse.error("资源类型不支持");
        }
        resourceCategoryService.create(req.getType(), req.getSort(), req.getName());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        ResourceCategory category = resourceCategoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated ResourceCategoryRequest req) throws NotFoundException {
        if (!Arrays.asList(BackendConstant.RESOURCE_EXT_WHITELIST).contains(req.getType())) {
            return JsonResponse.error("资源类型不支持");
        }
        ResourceCategory category = resourceCategoryService.findOrFail(id);
        resourceCategoryService.update(category, req.getSort(), req.getName());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        resourceCategoryService.removeById(id);
        ctx.publishEvent(new ResourceCategoryDestroyEvent(this, PlayEduBackendThreadLocal.getAdminUserID(), id, new Date()));
        return JsonResponse.success();
    }

}
