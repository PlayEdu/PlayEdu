package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.mapper.ResourceCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        return list(query().getWrapper().eq("type", type).orderByAsc("sort"));
    }

    @Override
    public void create(String type, Integer sort, String name) {
        ResourceCategory category = new ResourceCategory();

        category.setType(type);
        category.setSort(sort);
        category.setName(name);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        save(category);
    }

    @Override
    public ResourceCategory findOrFail(Integer id) throws NotFoundException {
        ResourceCategory category = getOne(query().getWrapper().eq("id", id));
        if (category == null) {
            throw new NotFoundException("分类不存在");
        }
        return category;
    }

    @Override
    public void update(ResourceCategory category, Integer sort, String name) {
        ResourceCategory newCategory = new ResourceCategory();
        newCategory.setId(category.getId());
        newCategory.setSort(sort);
        newCategory.setName(name);
        updateById(newCategory);
    }

    @Override
    public ResourceCategory find(Integer id, String type) {
        return getOne(query().getWrapper().eq("id", id).eq("type", type));
    }
}




