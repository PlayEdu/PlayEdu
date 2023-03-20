package xyz.playedu.api.controller.backend;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBCtx;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.ResourceDestroyMultiRequest;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 10:50
 */
@RestController
@RequestMapping("/backend/v1/resource")
public class ResourceController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceVideoService resourceVideoService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private BackendBus backendBus;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String type = MapUtils.getString(params, "type");
        String categoryIds = MapUtils.getString(params, "category_ids");

        if (type == null || type.trim().length() == 0) {
            return JsonResponse.error("请选择资源类型");
        }

        ResourcePaginateFilter filter = new ResourcePaginateFilter();
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setType(type);
        filter.setCategoryIds(categoryIds);
        filter.setName(name);

        if (!backendBus.isSuperAdmin()) {// 非超管只能读取它自己上传的资源
            filter.setAdminId(PlayEduBCtx.getAdminUserID());
        }

        PaginationResult<Resource> result = resourceService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("result", result);
        data.put("category_count", resourceService.getCategoryCount(type));
        data.put("pure_total", resourceService.total(type));

        if (type.equals(BackendConstant.RESOURCE_TYPE_VIDEO)) {
            List<ResourceVideo> resourceVideos = resourceVideoService.chunksByRids(result.getData().stream().map(Resource::getId).toList());
            Map<Integer, ResourceVideo> resourceVideosExtra = resourceVideos.stream().collect(Collectors.toMap(ResourceVideo::getRid, e -> e));
            data.put("videos_extra", resourceVideosExtra);
        }

        // 操作人
        data.put("admin_users", new HashMap<>());
        if (result.getData().size() > 0) {
            Map<Integer, String> adminUsers = adminUserService.chunks(result.getData().stream().map(Resource::getAdminId).toList()).stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getName));
            data.put("admin_users", adminUsers);
        }

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_DESTROY)
    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Resource resource = resourceService.findOrFail(id);
        // 删除文件
        minioService.removeByPath(resource.getPath());
        // 如果是视频资源文件则删除对应的时长关联记录
        if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(resource.getType())) {
            resourceVideoService.removeByRid(resource.getId());
        }
        // 删除资源记录
        resourceService.removeById(resource.getId());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_DESTROY)
    @PostMapping("/destroy-multi")
    @Transactional
    public JsonResponse multiDestroy(@RequestBody ResourceDestroyMultiRequest req) {
        if (req.getIds() == null || req.getIds().size() == 0) {
            return JsonResponse.error("请选择需要删除的资源");
        }
        List<Resource> resources = resourceService.chunks(req.getIds());
        if (resources == null || resources.size() == 0) {
            return JsonResponse.success();
        }
        for (Resource resourceItem : resources) {
            minioService.removeByPath(resourceItem.getPath());
            if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(resourceItem.getType())) {
                resourceVideoService.removeByRid(resourceItem.getId());
            }
            resourceService.removeById(resourceItem.getId());
        }
        return JsonResponse.success();
    }

}
