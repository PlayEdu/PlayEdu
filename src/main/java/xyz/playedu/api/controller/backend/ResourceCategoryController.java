package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.ResourceCategory;
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
        data.put("types", BackendConstant.RESOURCE_TYPE_WHITELIST);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest request) {
        if (!Arrays.asList(BackendConstant.RESOURCE_TYPE_WHITELIST).contains(request.getType())) {
            return JsonResponse.error("资源类型不支持");
        }

        ResourceCategory category = new ResourceCategory();

        category.setType(request.getType());
        category.setSort(request.getSort());
        category.setName(request.getName());
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        resourceCategoryService.save(category);

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) {
        ResourceCategory category = resourceCategoryService.getById(id);
        if (category == null) {
            return JsonResponse.error("分类不存在");
        }
        return JsonResponse.data(category);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated ResourceCategoryRequest request) {
        if (!Arrays.asList(BackendConstant.RESOURCE_TYPE_WHITELIST).contains(request.getType())) {
            return JsonResponse.error("资源类型不支持");
        }

        ResourceCategory category = resourceCategoryService.getById(id);
        if (category == null) {
            return JsonResponse.error("分类不存在");
        }

        ResourceCategory newCategory = new ResourceCategory();
        newCategory.setId(category.getId());
        newCategory.setName(request.getName());
        newCategory.setSort(request.getSort());

        resourceCategoryService.updateById(newCategory);

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        resourceCategoryService.removeById(id);
        return JsonResponse.success();
    }

}
