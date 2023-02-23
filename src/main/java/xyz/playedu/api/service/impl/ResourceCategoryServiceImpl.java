package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.mapper.ResourceCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service实现
 * @createDate 2023-02-23 09:50:18
 */
@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {
    @Override
    public List<ResourceCategory> getByType(String type) {
        return list(query().getWrapper().eq("type", type).orderByAsc("id"));
    }
}




