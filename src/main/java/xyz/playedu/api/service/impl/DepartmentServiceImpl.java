package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.bus.DepartmentBus;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Service实现
 * @createDate 2023-02-19 10:39:57
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

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
        updateParentChain(department.getParentChain(), DepartmentBus.childrenParentChain(department));
        removeById(department.getId());
    }

    @Override
    @Transactional
    public void update(Department department, String name, Integer parentId, Integer sort) throws NotFoundException {
        //计算该部门作为其它子部门的parentChain值
        String childrenChainPrefix = DepartmentBus.childrenParentChain(department);

        Department data = new Department();
        data.setId(department.getId());

        if (!department.getName().equals(name)) {//更换部门名称
            data.setName(name);
        }

        if (!department.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            if (parentId.equals(0)) {//重置一级部门
                data.setParentChain("");
            } else {
                Department parentDepartment = findOrFail(parentId);
                data.setParentChain(DepartmentBus.childrenParentChain(parentDepartment));
            }
        }
        if (!department.getSort().equals(sort)) {//更换部门排序值
            data.setSort(sort);
        }

        //提交更换
        updateById(data);

        department = getById(department.getId());
        updateParentChain(DepartmentBus.childrenParentChain(department), childrenChainPrefix);
    }

    private void updateParentChain(String newChildrenPC, String oldChildrenPC) {
        List<Department> children = list(query().getWrapper().like("parent_chain", oldChildrenPC + "%"));
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
                pc = tmpDepartment.getParentChain().replaceFirst(oldChildrenPC + ",", newChildrenPC.length() == 0 ? newChildrenPC : newChildrenPC + ',');
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
}




