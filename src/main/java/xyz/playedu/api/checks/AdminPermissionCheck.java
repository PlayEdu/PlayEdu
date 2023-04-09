/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.service.AdminPermissionService;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/20 14:31
 */
@Component
public class AdminPermissionCheck implements ApplicationRunner {

    private final Map<String, Map<String, AdminPermission[]>> permissions =
            new HashMap<>() {
                {
                    put(
                            BPermissionConstant.TYPE_ACTION,
                            new HashMap<>() {
                                {
                                    // 管理员
                                    put(
                                            "管理员",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("列表");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .ADMIN_USER_INDEX);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("新增|编辑|删除");
                                                        setSlug(BPermissionConstant.ADMIN_USER_CUD);
                                                    }
                                                },
                                            });
                                    // 管理员角色
                                    put(
                                            "管理员角色",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("新增|编辑|删除");
                                                        setSlug(BPermissionConstant.ADMIN_ROLE);
                                                    }
                                                },
                                            });
                                    // 部门
                                    put(
                                            "部门",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("新增|编辑|删除");
                                                        setSlug(BPermissionConstant.DEPARTMENT_CUD);
                                                    }
                                                },
                                            });
                                    // 资源分类
                                    put(
                                            "资源分类",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("新增|编辑|删除");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .RESOURCE_CATEGORY);
                                                    }
                                                },
                                            });
                                    // 资源
                                    put(
                                            "资源",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("删除");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .RESOURCE_DESTROY);
                                                    }
                                                },
                                            });
                                    // 学员
                                    put(
                                            "学员",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("列表");
                                                        setSlug(BPermissionConstant.USER_INDEX);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("新增");
                                                        setSlug(BPermissionConstant.USER_STORE);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(20);
                                                        setName("编辑");
                                                        setSlug(BPermissionConstant.USER_UPDATE);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(30);
                                                        setName("删除");
                                                        setSlug(BPermissionConstant.USER_DESTROY);
                                                    }
                                                },
                                            });
                                    // 线上课
                                    put(
                                            "线上课",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("新增|编辑|删除");
                                                        setSlug(BPermissionConstant.COURSE);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("学员学习记录-列表");
                                                        setSlug(BPermissionConstant.COURSE_USER);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(20);
                                                        setName("学员学习记录-删除");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .COURSE_USER_DESTROY);
                                                    }
                                                },
                                            });
                                    // 其它
                                    put(
                                            "其它",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("修改登录密码");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .PASSWORD_CHANGE);
                                                    }
                                                },
                                            });
                                }
                            });
                    put(
                            BPermissionConstant.TYPE_DATA,
                            new HashMap<>() {
                                {
                                    // 管理员
                                    put(
                                            "管理员",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("邮箱");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .DATA_ADMIN_EMAIL);
                                                    }
                                                },
                                            });
                                    // 学员
                                    put(
                                            "学员",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("邮箱");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .DATA_USER_EMAIL);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("姓名");
                                                        setSlug(BPermissionConstant.DATA_USER_NAME);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(20);
                                                        setName("身份证号");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .DATA_USER_ID_CARD);
                                                    }
                                                },
                                            });
                                }
                            });
                }
            };

    @Autowired private AdminPermissionService permissionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        HashMap<String, Boolean> slugs = permissionService.allSlugs();
        List<AdminPermission> list = new ArrayList<>();
        Date now = new Date();

        permissions.forEach(
                (typeValue, group) -> {
                    group.forEach(
                            (groupNameValue, item) -> {
                                for (int i = 0; i < item.length; i++) {
                                    AdminPermission permissionItem = item[i];

                                    if (slugs.get(permissionItem.getSlug()) != null) {
                                        continue;
                                    }

                                    // 不存在
                                    list.add(
                                            new AdminPermission() {
                                                {
                                                    setType(typeValue);
                                                    setGroupName(groupNameValue);
                                                    setSort(permissionItem.getSort());
                                                    setName(permissionItem.getName());
                                                    setSlug(permissionItem.getSlug());
                                                    setCreatedAt(now);
                                                }
                                            });
                                }
                            });
                });

        if (list.size() > 0) {
            permissionService.saveBatch(list);
        }
    }
}
