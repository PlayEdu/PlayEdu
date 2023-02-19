package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.DepartmentService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/19 11:02
 */

@Component
public class DepartmentBus {

    @Autowired
    private DepartmentService departmentService;

    public String compParentChain(Integer parentId) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            Department parentDepartment = departmentService.getById(parentId);
            if (parentDepartment == null) {
                throw new NotFoundException("父级部门不存在");
            }
            String pc = parentDepartment.getParentChain();
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    public static String childrenParentChain(Department department) {
        String prefix = department.getId() + "";
        if (department.getParentChain() != null && department.getParentChain().length() > 0) {
            prefix = department.getParentChain() + "," + prefix;
        }
        return prefix;
    }

}
