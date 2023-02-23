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
        if (filter != null) {
            if (filter.getName() != null) {
                wrapper.like("name", "%" + filter.getName() + "%");
            }
            if (filter.getDisk() != null) {
                wrapper.eq("disk", filter.getDisk());
            }
            if (filter.getExtension() != null) {
                wrapper.eq("extension", filter.getExtension());
            }
        }

        IPage<Resource> adminPage = new Page<>(page, size);
        adminPage = page(adminPage, wrapper);

        PaginationResult<Resource> pageResult = new PaginationResult<>();
        pageResult.setData(adminPage.getRecords());
        pageResult.setTotal(adminPage.getTotal());

        return pageResult;
    }
}




