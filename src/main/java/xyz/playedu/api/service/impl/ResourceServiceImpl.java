package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceCategoryRelation;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.mapper.ResourceMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.service.internal.ResourceCategoryRelationService;
import xyz.playedu.api.types.mapper.ResourceCategoryCountMapper;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;
import xyz.playedu.api.util.HelperUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Service实现
 * @createDate 2023-02-23 10:50:26
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceVideoService resourceVideoService;

    @Autowired
    private ResourceCategoryRelationService relationService;

    @Override
    public PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter) {
        QueryWrapper<Resource> wrapper = query().getWrapper().eq("is_hidden", 0);

        if (filter.getName() != null) {
            wrapper.like("name", "%" + filter.getName() + "%");
        }
        if (filter.getDisk() != null) {
            wrapper.eq("disk", filter.getDisk());
        }
        if (filter.getExtension() != null) {
            wrapper.eq("extension", filter.getExtension());
        }
        if (filter.getType() != null) {
            wrapper.eq("type", filter.getType());
        }
        if (filter.getAdminId() != null && !filter.getAdminId().equals(0)) {
            wrapper.eq("admin_id", filter.getAdminId());
        }
        if (filter.getCategoryIds() != null && filter.getCategoryIds().trim().length() > 0) {
            List<Integer> categoryIds = Arrays.stream(filter.getCategoryIds().split(",")).map(Integer::valueOf).toList();
            List<Integer> ids = relationService.getRidsByCids(categoryIds);
            if (ids == null || ids.size() == 0) {
                ids = HelperUtil.zeroIntegerList();
            }
            wrapper.in("id", ids);
        }

        String sortFiled = filter.getSortField();
        if (sortFiled == null || sortFiled.trim().length() == 0) {
            sortFiled = "id";
        }
        String sortAlgo = filter.getSortAlgo();
        if (sortAlgo == null || sortAlgo.trim().length() == 0) {
            sortAlgo = "desc";
        }
        if ("desc".equals(sortAlgo)) {
            wrapper.orderByDesc(sortFiled);
        } else {
            wrapper.orderByAsc(sortFiled);
        }

        IPage<Resource> adminPage = new Page<>(page, size);
        adminPage = page(adminPage, wrapper);

        PaginationResult<Resource> pageResult = new PaginationResult<>();
        pageResult.setData(adminPage.getRecords());
        pageResult.setTotal(adminPage.getTotal());

        return pageResult;
    }

    @Override
    @Transactional
    public Resource create(Integer adminId, String categoryIds, String type, String filename, String ext, Long size, String disk, String fileId, String path, String url) {
        Resource resource = new Resource();
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setFileId(fileId);
        resource.setPath(path);
        resource.setUrl(url);
        resource.setCreatedAt(new Date());
        save(resource);


        if (categoryIds != null && categoryIds.trim().length() > 0) {
            String[] idArray = categoryIds.split(",");
            List<ResourceCategoryRelation> relations = new ArrayList<>();
            for (int i = 0; i < idArray.length; i++) {
                String tmpId = idArray[i];

                relations.add(new ResourceCategoryRelation() {{
                    setCid(Integer.valueOf(tmpId));
                    setRid(resource.getId());
                }});
            }
            relationService.saveBatch(relations);
        }
        return resource;
    }

    @Override
    public Resource findOrFail(Integer id) throws NotFoundException {
        Resource resource = getById(id);
        if (resource == null) {
            throw new NotFoundException("资源不存在");
        }
        return resource;
    }

    @Override
    public void changeParentId(Integer id, Integer parentId) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setParentId(parentId);
        resource.setIsHidden(1);
        updateById(resource);
    }

    @Override
    public void storeResourceVideo(Integer rid, Integer duration, String poster) {
        resourceVideoService.create(rid, duration, poster);
    }

    @Override
    public List<Resource> chunks(List<Integer> ids) {
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public List<Resource> chunks(List<Integer> ids, List<String> fields) {
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public Map<Integer, Integer> getCategoryCount(String type) {
        Map<Integer, Integer> data = getBaseMapper().getCategoryCount(type).stream().collect(Collectors.toMap(ResourceCategoryCountMapper::getCid, ResourceCategoryCountMapper::getTotal));
        data.put(0, getBaseMapper().getNoneCategoryCount(type));
        return data;
    }

    @Override
    public Integer total(String type) {
        return Math.toIntExact(count(query().getWrapper().eq("type", type)));
    }
}




