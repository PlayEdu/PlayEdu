/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.domain.ResourceCategoryRelation;
import xyz.playedu.api.domain.ResourceCourseCategory;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.ResourceCategoryMapper;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.internal.ResourceCategoryRelationService;
import xyz.playedu.api.service.internal.ResourceCourseCategoryService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service实现
 * @createDate 2023-02-23 09:50:18
 */
@Service
public class ResourceCategoryServiceImpl
        extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {

    @Autowired private ResourceCourseCategoryService resourceCourseCategoryService;

    @Autowired private ResourceCategoryRelationService resourceCategoryRelationService;

    @Override
    public List<ResourceCategory> listByParentId(Integer id) {
        return list(query().getWrapper().eq("parent_id", id).orderByAsc("sort"));
    }

    @Override
    public List<ResourceCategory> all() {
        return list(query().getWrapper().orderByAsc("sort"));
    }

    @Override
    public ResourceCategory findOrFail(Integer id) throws NotFoundException {
        ResourceCategory category = getById(id);
        if (category == null) {
            throw new NotFoundException("分类不存在");
        }
        return category;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        ResourceCategory category = findOrFail(id);
        // 更新parent_chain
        updateParentChain(category.getParentChain(), childrenParentChain(category));
        // 删除记录
        removeById(category.getId());
    }

    @Override
    @Transactional
    public void update(ResourceCategory category, String name, Integer parentId, Integer sort)
            throws NotFoundException {
        String childrenChainPrefix = childrenParentChain(category);

        ResourceCategory data = new ResourceCategory();
        data.setId(category.getId());
        data.setName(name);

        if (!category.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            if (parentId.equals(0)) {
                data.setParentChain("");
            } else {
                ResourceCategory parentResourceCategory = findOrFail(parentId);
                data.setParentChain(childrenParentChain(parentResourceCategory));
            }
        }
        if (!category.getSort().equals(sort)) {
            data.setSort(sort);
        }

        // 提交更换
        updateById(data);

        category = getById(category.getId());
        updateParentChain(childrenParentChain(category), childrenChainPrefix);
    }

    private void updateParentChain(String newChildrenPC, String oldChildrenPC) {
        List<ResourceCategory> children =
                list(query().getWrapper().like("parent_chain", oldChildrenPC + "%"));
        if (children.size() == 0) {
            return;
        }

        ArrayList<ResourceCategory> updateRows = new ArrayList<>();
        for (ResourceCategory tmpResourceCategory : children) {
            ResourceCategory tmpUpdateResourceCategory = new ResourceCategory();
            tmpUpdateResourceCategory.setId(tmpResourceCategory.getId());

            // parentChain计算
            String pc = newChildrenPC;
            if (!tmpResourceCategory.getParentChain().equals(oldChildrenPC)) {
                pc =
                        tmpResourceCategory
                                .getParentChain()
                                .replaceFirst(
                                        oldChildrenPC + ",",
                                        newChildrenPC.length() == 0
                                                ? newChildrenPC
                                                : newChildrenPC + ',');
            }
            tmpUpdateResourceCategory.setParentChain(pc);

            // parentId计算
            int parentId = 0;
            if (pc != null && pc.length() > 0) {
                String[] parentIds = pc.split(",");
                parentId = Integer.parseInt(parentIds[parentIds.length - 1]);
            }
            tmpUpdateResourceCategory.setParentId(parentId);

            updateRows.add(tmpUpdateResourceCategory);
        }
        updateBatchById(updateRows);
    }

    @Override
    public void create(String name, Integer parentId, Integer sort) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            parentChain = compParentChain(parentId);
        }

        ResourceCategory category = new ResourceCategory();
        category.setName(name);
        category.setParentId(parentId);
        category.setParentChain(parentChain);
        category.setSort(sort);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        save(category);
    }

    @Override
    public String childrenParentChain(ResourceCategory category) {
        String prefix = category.getId() + "";
        if (category.getParentChain() != null && category.getParentChain().length() > 0) {
            prefix = category.getParentChain() + "," + prefix;
        }
        return prefix;
    }

    @Override
    public String compParentChain(Integer parentId) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            ResourceCategory parentResourceCategory = getById(parentId);
            if (parentResourceCategory == null) {
                throw new NotFoundException("父级分类不存在");
            }
            String pc = parentResourceCategory.getParentChain();
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    @Override
    public List<Integer> getCourseIdsById(Integer id) {
        return resourceCourseCategoryService
                .list(resourceCourseCategoryService.query().getWrapper().eq("category_id", id))
                .stream()
                .map(ResourceCourseCategory::getCourseId)
                .toList();
    }

    @Override
    public List<Integer> getRidsById(Integer id) {
        return resourceCategoryRelationService
                .list(resourceCategoryRelationService.query().getWrapper().eq("cid", id))
                .stream()
                .map(ResourceCategoryRelation::getRid)
                .toList();
    }

    @Override
    public void resetSort(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<ResourceCategory> categories = new ArrayList<>();
        int sortVal = 0;
        for (Integer idItem : ids) {
            Integer finalSortVal = ++sortVal;
            categories.add(
                    new ResourceCategory() {
                        {
                            setId(idItem);
                            setSort(finalSortVal);
                        }
                    });
        }
        updateBatchById(categories);
    }

    @Override
    @Transactional
    public void changeParent(Integer id, Integer parentId, List<Integer> ids)
            throws NotFoundException {
        ResourceCategory category = findOrFail(id);
        update(category, category.getName(), parentId, category.getSort());
        // 重置排序
        resetSort(ids);
    }

    @Override
    public Map<Integer, List<ResourceCategory>> groupByParent() {
        return list(query().getWrapper().orderByAsc("sort")).stream()
                .collect(Collectors.groupingBy(ResourceCategory::getParentId));
    }

    @Override
    public Map<Integer, String> id2name() {
        return all().stream()
                .collect(Collectors.toMap(ResourceCategory::getId, ResourceCategory::getName));
    }

    @Override
    public Long total() {
        return count();
    }
}
