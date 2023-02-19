package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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
        return this.getBaseMapper().selectList(query().eq("parent_id", id));
    }

    @Override
    public Department findOrFail(Integer id) throws NotFoundException {
        Department department = this.getBaseMapper().selectById(id);
        if (department == null) {
            throw new NotFoundException("部门不存在");
        }
        return department;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        Department department = findOrFail(id);
        updateParentChain(department);
        removeById(department.getId());
    }

    @Override
    @Transactional
    public void update(Department department, String name, Integer parentId, Integer sort) throws NotFoundException {
        Department data = new Department();
        if (!department.getName().equals(name)) {
            data.setName(name);
        }
        if (!department.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            Department parent = findOrFail(parentId);
            data.setParentChain(parent.getParentChain() + "," + parent.getId());
        }
        if (!department.getSort().equals(sort)) {
            data.setSort(sort);
        }
        save(data);

        department = getById(department.getId());
        updateParentChain(department);
    }

    private void updateParentChain(Department department) {
        if (department.getParentId().equals(0)) {
            return;
        }

        //需要重置chain的子部门
        List<Department> children = list(query().like("parent_chain", "%" + department.getParentChain()));
        if (children.size() == 0) {
            return;
        }

        // 计算新的parentChain前缀
        String[] chainIds = department.getParentChain().split(",");
        String newChainPrefix = String.join(",", Arrays.copyOfRange(chainIds, 0, chainIds.length - 1));

        log.info("新的前缀:" + newChainPrefix);

        ArrayList<Department> updateRows = new ArrayList<>();
        for (Department tmpDepartment : children) {
            Department tmpUpdateDepartment = new Department();
            tmpUpdateDepartment.setId(tmpDepartment.getId());
            tmpUpdateDepartment.setParentChain(tmpDepartment.getParentChain().replaceFirst(department.getParentChain(), newChainPrefix));

            updateRows.add(tmpUpdateDepartment);
        }
        updateBatchById(updateRows);
    }
}




