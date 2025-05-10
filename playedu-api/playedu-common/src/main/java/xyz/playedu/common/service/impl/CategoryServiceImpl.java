/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.common.domain.Category;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.mapper.CategoryMapper;
import xyz.playedu.common.service.CategoryService;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service实现
 * @createDate 2023-02-23 09:50:18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Override
    public List<Category> listByParentId(Integer id) {
        return list(query().getWrapper().eq("parent_id", id).orderByAsc("sort"));
    }

    @Override
    public List<Category> all() {
        return list(query().getWrapper().orderByAsc("sort"));
    }

    @Override
    public Category findOrFail(Integer id) throws NotFoundException {
        Category category = getById(id);
        if (category == null) {
            throw new NotFoundException("分类不存在");
        }
        return category;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        Category category = findOrFail(id);
        // 更新parent_chain
        updateParentChain(category.getParentChain(), childrenParentChain(category));
        // 删除记录
        removeById(category.getId());
    }

    @Override
    @Transactional
    public void update(Category category, String name, Integer parentId, Integer sort)
            throws NotFoundException {
        String childrenChainPrefix = childrenParentChain(category);

        Category data = new Category();
        data.setId(category.getId());
        data.setName(name);

        if (!category.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            if (parentId.equals(0)) {
                data.setParentChain("");
            } else {
                Category parentResourceCategory = findOrFail(parentId);
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
        List<Category> children =
                list(query().getWrapper().like("parent_chain", oldChildrenPC + "%"));
        if (children.size() == 0) {
            return;
        }

        ArrayList<Category> updateRows = new ArrayList<>();
        for (Category tmpResourceCategory : children) {
            Category tmpUpdateResourceCategory = new Category();
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

        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setParentChain(parentChain);
        category.setSort(sort);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        save(category);
    }

    @Override
    public String childrenParentChain(Category category) {
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
            Category parentResourceCategory = getById(parentId);
            if (parentResourceCategory == null) {
                throw new NotFoundException("父级分类不存在");
            }
            String pc = parentResourceCategory.getParentChain();
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    @Override
    public void resetSort(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<Category> categories = new ArrayList<>();
        int sortVal = 0;
        for (Integer idItem : ids) {
            Integer finalSortVal = ++sortVal;
            categories.add(
                    new Category() {
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
        Category category = findOrFail(id);
        update(category, category.getName(), parentId, category.getSort());
        // 重置排序
        resetSort(ids);
    }

    @Override
    public Map<Integer, List<Category>> groupByParent() {
        return list(query().getWrapper().orderByAsc("sort")).stream()
                .collect(Collectors.groupingBy(Category::getParentId));
    }

    @Override
    public Map<Integer, String> id2name() {
        return all().stream().collect(Collectors.toMap(Category::getId, Category::getName));
    }

    @Override
    public Long total() {
        return count();
    }

    @Override
    public List<Category> getChildCategorysByParentId(Integer parentId) {
        return list(
                query().getWrapper()
                        .eq("parent_id", parentId)
                        .or()
                        .likeRight("parent_chain", parentId + ","));
    }
}
