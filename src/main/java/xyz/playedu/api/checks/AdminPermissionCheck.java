package xyz.playedu.api.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.service.AdminPermissionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/20 14:31
 */
@Component
public class AdminPermissionCheck implements ApplicationRunner {

    @Autowired
    private AdminPermissionService permissionService;

    private final String[][] ACTION_PERMISSIONS = {
            {"管理员", "0", "管理员-查看", BPermissionConstant.ADMIN_USER_INDEX},
            {"管理员", "5", "管理员-添加", BPermissionConstant.ADMIN_USER_STORE},
            {"管理员", "10", "管理员-编辑", BPermissionConstant.ADMIN_USER_UPDATE},
            {"管理员", "15", "管理员-删除", BPermissionConstant.ADMIN_USER_DESTROY},

            {"管理员角色", "0", "管理员角色", BPermissionConstant.ADMIN_ROLE},

            {"部门", "5", "部门-添加", BPermissionConstant.DEPARTMENT_STORE},
            {"部门", "10", "部门-编辑", BPermissionConstant.DEPARTMENT_UPDATE},
            {"部门", "15", "部门-删除", BPermissionConstant.DEPARTMENT_DESTROY},

            {"资源分类", "0", "资源分类", BPermissionConstant.RESOURCE_CATEGORY},

            {"学员", "0", "学员-查看", BPermissionConstant.USER_INDEX},
            {"学员", "5", "学员-添加", BPermissionConstant.USER_STORE},
            {"学员", "10", "学员-编辑", BPermissionConstant.USER_UPDATE},
            {"学员", "15", "学员-删除", BPermissionConstant.USER_DESTROY},

            {"课程分类", "0", "课程分类", BPermissionConstant.COURSE_CATEGORY},
            {"课程", "0", "课程", BPermissionConstant.COURSE},
    };

    private final String[][] DATA_PERMISSIONS = {
            {"管理员", "0", "邮箱", BPermissionConstant.DATA_ADMIN_EMAIL},

            {"学员", "0", "邮箱", BPermissionConstant.DATA_USER_EMAIL},
            {"学员", "10", "姓名", BPermissionConstant.DATA_USER_NAME},
            {"学员", "10", "身份证号", BPermissionConstant.DATA_USER_ID_CARD},
    };

    @Override
    public void run(ApplicationArguments args) throws Exception {
        HashMap<String, Boolean> slugs = permissionService.allSlugs();
        List<AdminPermission> list = new ArrayList<>();
        Date now = new Date();

        for (int i = 0; i < ACTION_PERMISSIONS.length; i++) {
            String[] item = ACTION_PERMISSIONS[i];
            String tmpSlug = item[3];
            if (slugs.get(tmpSlug) != null) {//已经存在
                continue;
            }
            AdminPermission permission = new AdminPermission();

            permission.setGroupName(item[0]);
            permission.setSort(Integer.valueOf(item[1]));
            permission.setName(item[2]);
            permission.setSlug(tmpSlug);
            permission.setType(BPermissionConstant.TYPE_ACTION);
            permission.setCreatedAt(now);

            list.add(permission);
        }

        for (int i = 0; i < DATA_PERMISSIONS.length; i++) {
            String[] item = DATA_PERMISSIONS[i];
            String tmpSlug = item[3];
            if (slugs.get(tmpSlug) != null) {//已经存在
                continue;
            }
            AdminPermission permission = new AdminPermission();

            permission.setGroupName(item[0]);
            permission.setSort(Integer.valueOf(item[1]));
            permission.setName(item[2]);
            permission.setSlug(tmpSlug);
            permission.setType(BPermissionConstant.TYPE_DATA);
            permission.setCreatedAt(now);

            list.add(permission);
        }

        permissionService.saveBatch(list);
    }
}
