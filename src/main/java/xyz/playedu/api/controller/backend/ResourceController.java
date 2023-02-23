package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.request.backend.ResourceRequest;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 10:50
 */
@RestController
@RequestMapping("/backend/v1/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceCategoryService categoryService;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size, @RequestParam(name = "name", defaultValue = "") String name) {
        ResourcePaginateFilter filter = new ResourcePaginateFilter();
        if (name != null && name.length() > 0) {
            filter.setName(name);
        }

        PaginationResult<Resource> result = resourceService.paginate(page, size, filter);

        return JsonResponse.data(result);
    }

    @GetMapping("/create")
    public JsonResponse create(@RequestParam(name = "type") String type) {
        List<ResourceCategory> categories = categoryService.getByType(type);
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        return JsonResponse.data(data);
    }

    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated ResourceRequest request) {
        if (categoryService.getById(request.getCategoryId()) == null) {
            return JsonResponse.error("资源分类不存在");
        }
        if (!Arrays.asList(BackendConstant.RESOURCE_DISK_WHITELIST).contains(request.getDisk())) {
            return JsonResponse.error("存储磁盘参数错误");
        }

        Resource resource = new Resource();

        resource.setCategoryId(request.getCategoryId());
        resource.setName(request.getName());
        resource.setExtension(request.getExtension());
        resource.setSize(request.getSize());
        resource.setDisk(request.getDisk());
        resource.setFileId(request.getFileId());
        resource.setPath(request.getPath());
        resource.setUrl(request.getUrl());
        resource.setCreatedAt(new Date());

        resourceService.save(resource);

        return JsonResponse.success();
    }

    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        resourceService.removeById(id);
        return JsonResponse.success();
    }

}
