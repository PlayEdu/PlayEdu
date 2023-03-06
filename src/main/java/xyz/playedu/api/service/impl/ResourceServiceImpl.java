package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.mapper.ResourceMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Service实现
 * @createDate 2023-02-23 10:50:26
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Override
    public PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter) {
        QueryWrapper<Resource> wrapper = query().getWrapper().eq("1", "1");

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
        if (filter.getCategoryIds() != null && filter.getCategoryIds().length > 0) {
            wrapper.in("category_id", Arrays.asList(filter.getCategoryIds()));
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
    public Resource create(Integer categoryId, String type, String filename, String ext, Long size, String disk, String fileId, String path, String url) {
        Resource resource = new Resource();
        resource.setType(type);
        resource.setCategoryId(categoryId);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setFileId(fileId);
        resource.setPath(path);
        resource.setUrl(url);
        resource.setCreatedAt(new Date());
        save(resource);
        return resource;
    }
}




