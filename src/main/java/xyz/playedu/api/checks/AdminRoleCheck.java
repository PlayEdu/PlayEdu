package xyz.playedu.api.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.service.AdminRoleService;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 22:09
 */
@Component
public class AdminRoleCheck implements ApplicationRunner {

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AdminRole adminRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);
        if (adminRole != null) {//已存在超级管理权限
            return;
        }
        adminRole = new AdminRole();

        adminRole.setName("超级管理角色");
        adminRole.setSlug(BackendConstant.SUPER_ADMIN_ROLE);
        adminRole.setCreatedAt(new Date());
        adminRole.setUpdatedAt(new Date());

        adminRoleService.save(adminRole);
    }

}
