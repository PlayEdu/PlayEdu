package xyz.playedu.api.controller.backend;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.exception.NotFoundException;
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
    private ResourceService resourceService;

    @Autowired
    private ResourceVideoService resourceVideoService;

    @Autowired
    private MinioService minioService;

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

        HashMap<String, Object> data = new HashMap<>();
        data.put("result", result);

        if (type.equals(BackendConstant.RESOURCE_TYPE_VIDEO)) {
            List<ResourceVideo> resourceVideos = resourceVideoService.chunksByRids(result.getData().stream().map(Resource::getId).toList());
            Map<Integer, ResourceVideo> resourceVideosExtra = resourceVideos.stream().collect(Collectors.toMap(ResourceVideo::getRid, e -> e));
            data.put("videos_extra", resourceVideosExtra);
        }

        return JsonResponse.data(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Resource resource = resourceService.findOrFail(id);
        // 删除文件
        minioService.removeByPath(resource.getPath());
        // 如果是视频资源文件则删除对应的时长关联记录
        if (resource.getType().equals(BackendConstant.RESOURCE_TYPE_VIDEO)) {
            resourceVideoService.removeByRid(resource.getId());
        }
        // 删除资源记录
        resourceService.removeById(resource.getId());
        return JsonResponse.success();
    }

}
