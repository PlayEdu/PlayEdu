package xyz.playedu.api.controller.backend;

import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.config.MinioConfig;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.request.backend.ResourceRequest;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 10:50
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceVideoService resourceVideoService;

    @Autowired
    private ResourceCategoryService categoryService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String type = MapUtils.getString(params, "type");
        String categoryIdsStr = MapUtils.getString(params, "category_ids");

        if (type == null || type.trim().length() == 0) {
            return JsonResponse.error("请选择资源类型");
        }

        ResourcePaginateFilter filter = new ResourcePaginateFilter();
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setType(type);
        if (name != null && name.length() > 0) {
            filter.setName(name);
        }
        if (categoryIdsStr != null && categoryIdsStr.length() != 0) {
            Integer[] categoryIds = Arrays.stream(categoryIdsStr.split(",")).map(Integer::valueOf).toArray(Integer[]::new);
            filter.setCategoryIds(categoryIds);
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
    @Transactional
    public JsonResponse store(@RequestBody @Validated ResourceRequest req) {
        String disk = req.getDisk();
        if (!Arrays.asList(BackendConstant.RESOURCE_DISK_WHITELIST).contains(disk)) {
            return JsonResponse.error("存储磁盘参数错误");
        }
        String extension = req.getExtension().toLowerCase();
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(extension);
        if (type == null) {
            return JsonResponse.error("格式不支持");
        }

        // 如果是视频则必须传递duration参数
        Integer duration = req.getDuration();
        boolean isVideoType = BackendConstant.RESOURCE_TYPE_VIDEO.equals(type);
        if (isVideoType) {
            if (duration == null || duration == 0) {
                return JsonResponse.error("duration参数必须存在且大于0");
            }
        }

        Resource res = resourceService.create(req.getCategoryId(), type, req.getName(), extension, req.getSize(), disk, req.getFileId(), req.getPath(), req.getUrl());

        if (isVideoType) {
            resourceVideoService.create(res.getId(), duration);
        }

        return JsonResponse.data(res);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Resource resource = resourceService.findOrFail(id);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucket()).object(resource.getPath()).build());
            if (resource.getType().equals(BackendConstant.RESOURCE_TYPE_VIDEO)) {
                resourceVideoService.removeByRid(resource.getId());
            }
            resourceService.removeById(resource.getId());
            return JsonResponse.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResponse.error("系统错误");
        }
    }

}
