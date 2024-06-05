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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.common.domain.Department;
import xyz.playedu.common.domain.UserDepartment;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.mapper.DepartmentMapper;
import xyz.playedu.common.service.DepartmentService;
import xyz.playedu.common.service.UserDepartmentService;
import xyz.playedu.common.types.mapper.DepartmentsUserCountMapRes;
import xyz.playedu.common.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Service实现
 * @createDate 2023-02-19 10:39:57
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Autowired private UserDepartmentService userDepartmentService;

    @Override
    public List<Department> listByParentId(Integer id) {
        return list(query().getWrapper().eq("parent_id", id).orderByAsc("sort"));
    }

    @Override
    public List<Department> all() {
        return list(query().getWrapper().orderByAsc("sort"));
    }

    @Override
    public Department findOrFail(Integer id) throws NotFoundException {
        Department department = getById(id);
        if (department == null) {
            throw new NotFoundException("部门不存在");
        }
        return department;
    }

    @Override
    @Transactional
    public void destroy(Integer id) throws NotFoundException {
        Department department = findOrFail(id);
        updateParentChain(department.getParentChain(), childrenParentChain(department));
        removeById(department.getId());
    }

    @Override
    @Transactional
    public void update(Department department, String name, Integer parentId, Integer sort)
            throws NotFoundException {
        // 计算该部门作为其它子部门的parentChain值
        String childrenChainPrefix = childrenParentChain(department);

        Department data = new Department();
        data.setId(department.getId());
        data.setName(name);

        if (!department.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            if (parentId.equals(0)) { // 重置一级部门
                data.setParentChain("");
            } else {
                Department parentDepartment = findOrFail(parentId);
                data.setParentChain(childrenParentChain(parentDepartment));
            }
        }
        if (!department.getSort().equals(sort)) { // 更换部门排序值
            data.setSort(sort);
        }

        // 提交更换
        updateById(data);

        department = getById(department.getId());
        updateParentChain(childrenParentChain(department), childrenChainPrefix);
    }

    private void updateParentChain(String newChildrenPC, String oldChildrenPC) {
        List<Department> children =
                list(query().getWrapper().like("parent_chain", oldChildrenPC + "%"));
        if (children.isEmpty()) {
            return;
        }

        ArrayList<Department> updateRows = new ArrayList<>();
        for (Department tmpDepartment : children) {
            Department tmpUpdateDepartment = new Department();
            tmpUpdateDepartment.setId(tmpDepartment.getId());

            // parentChain计算
            String pc = newChildrenPC;
            if (!tmpDepartment.getParentChain().equals(oldChildrenPC)) {
                pc =
                        tmpDepartment
                                .getParentChain()
                                .replaceFirst(
                                        oldChildrenPC + ",",
                                        newChildrenPC.isEmpty()
                                                ? newChildrenPC
                                                : newChildrenPC + ',');
            }
            tmpUpdateDepartment.setParentChain(pc);

            // parentId计算
            int parentId = 0;
            if (pc != null && !pc.isEmpty()) {
                String[] parentIds = pc.split(",");
                parentId = Integer.parseInt(parentIds[parentIds.length - 1]);
            }
            tmpUpdateDepartment.setParentId(parentId);

            updateRows.add(tmpUpdateDepartment);
        }
        updateBatchById(updateRows);
    }

    @Override
    public String compParentChain(Integer parentId) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            Department parentDepartment = getById(parentId);
            if (parentDepartment == null) {
                throw new NotFoundException("父级部门不存在");
            }
            String pc = parentDepartment.getParentChain();
            parentChain = pc == null || pc.isEmpty() ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    @Override
    public String childrenParentChain(Department department) {
        String prefix = department.getId() + "";
        if (department.getParentChain() != null && !department.getParentChain().isEmpty()) {
            prefix = department.getParentChain() + "," + prefix;
        }
        return prefix;
    }

    @Override
    public Integer create(String name, Integer parentId, Integer sort) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            parentChain = compParentChain(parentId);
        }

        Department department = new Department();
        department.setName(name);
        department.setParentId(parentId);
        department.setParentChain(parentChain);
        department.setSort(sort);
        department.setCreatedAt(new Date());
        department.setUpdatedAt(new Date());

        save(department);

        return department.getId();
    }

    @Override
    public void remoteRelateUsersByDepId(Integer depId) {
        QueryWrapper<UserDepartment> wrapper =
                userDepartmentService.query().getWrapper().eq("dep_id", depId);
        userDepartmentService.remove(wrapper);
    }

    @Override
    public List<Integer> getUserIdsByDepId(Integer depId) {
        return userDepartmentService
                .list(userDepartmentService.query().getWrapper().eq("dep_id", depId))
                .stream()
                .map(UserDepartment::getUserId)
                .toList();
    }

    @Override
    @Transactional
    public void changeParent(Integer id, Integer parentId, List<Integer> ids)
            throws NotFoundException {
        Department department = findOrFail(id);
        update(department, department.getName(), parentId, department.getSort());
        // 重置排序
        resetSort(ids);
    }

    @Override
    public void resetSort(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<Department> departments = new ArrayList<>();
        int sortVal = 0;
        for (Integer idItem : ids) {
            Integer finalSortVal = ++sortVal;
            departments.add(
                    new Department() {
                        {
                            setId(idItem);
                            setSort(finalSortVal);
                        }
                    });
        }
        updateBatchById(departments);
    }

    @Override
    public Map<Integer, List<Department>> groupByParent() {
        return list(query().getWrapper().orderByAsc("sort")).stream()
                .collect(Collectors.groupingBy(Department::getParentId));
    }

    @Override
    public Map<Integer, String> id2name() {
        return all().stream().collect(Collectors.toMap(Department::getId, Department::getName));
    }

    @Override
    public Long total() {
        return count();
    }

    @Override
    public Map<Integer, Integer> getDepartmentsUserCount() {
        return getBaseMapper().getDepartmentsUserCount().stream()
                .collect(
                        Collectors.toMap(
                                DepartmentsUserCountMapRes::getDepId,
                                DepartmentsUserCountMapRes::getTotal));
    }

    @Override
    public List<Department> chunk(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    @SneakyThrows
    public Integer createWithChainList(List<String> ou) {
        if (ou == null || ou.isEmpty()) {
            return 0;
        }

        Department department = null;
        for (int i = 0; i < ou.size(); i++) {
            String name = ou.get(i);
            Integer parentId = department == null ? 0 : department.getId();
            department = getOne(query().getWrapper().eq("name", name).eq("parent_id", parentId));
            if (department == null) {
                Integer depId = create(name, parentId, i);
                // refresh
                department = new Department();
                department.setId(depId);
            }
        }
        return department.getId();
    }

    @Override
    public Department findByName(String name, Integer parentId) {
        return getOne(query().getWrapper().eq("name", name).eq("parent_id", parentId));
    }

    @Override
    public List<Department> getChildDepartmentsByParentId(Integer parentId) {
        return list(
                query().getWrapper()
                        .eq("parent_id", parentId)
                        .or()
                        .likeRight("parent_chain", parentId + ","));
    }

    @Override
    public List<Department> getChildDepartmentsByParentChain(Integer parentId, String parentChain) {
        if (StringUtil.isEmpty(parentChain)) {
            return new ArrayList<>();
        }
        return list(
                query().getWrapper()
                        .eq("parent_id", parentId)
                        .or()
                        .likeRight("parent_chain", parentChain + ","));
    }
}
