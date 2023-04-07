/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.DepartmentMapper;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.internal.UserDepartmentService;

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

    @Autowired private CourseDepartmentService courseDepartmentService;

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
    public void deleteById(Integer id) throws NotFoundException {
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
        if (children.size() == 0) {
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
                                        newChildrenPC.length() == 0
                                                ? newChildrenPC
                                                : newChildrenPC + ',');
            }
            tmpUpdateDepartment.setParentChain(pc);

            // parentId计算
            int parentId = 0;
            if (pc != null && pc.length() > 0) {
                String[] parentIds = pc.split(",");
                parentId = Integer.parseInt(parentIds[parentIds.length - 1]);
            }
            tmpUpdateDepartment.setParentId(parentId);

            updateRows.add(tmpUpdateDepartment);
        }
        updateBatchById(updateRows);
    }

    @Override
    public List<Integer> allIds() {
        List<Department> departments = list(query().getWrapper().eq("1", "1").select("id"));
        List<Integer> ids = new ArrayList<>();
        for (Department department : departments) {
            ids.add(department.getId());
        }
        return ids;
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
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    @Override
    public String childrenParentChain(Department department) {
        String prefix = department.getId() + "";
        if (department.getParentChain() != null && department.getParentChain().length() > 0) {
            prefix = department.getParentChain() + "," + prefix;
        }
        return prefix;
    }

    @Override
    public void create(String name, Integer parentId, Integer sort) throws NotFoundException {
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
    public List<Integer> getCourseIdsByDepId(Integer depId) {
        return courseDepartmentService
                .list(courseDepartmentService.query().getWrapper().eq("dep_id", depId))
                .stream()
                .map(CourseDepartment::getCourseId)
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
}
