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
package xyz.playedu.system.checks;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.domain.AdminPermission;
import xyz.playedu.common.service.AdminPermissionService;

@Order(1020)
@Component
public class AdminPermissionCheck implements CommandLineRunner {

    private final Map<String, Map<String, AdminPermission[]>> permissions =
            new HashMap<>() {
                {
                    put(
                            BPermissionConstant.TYPE_ACTION,
                            new HashMap<>() {
                                {
                                    // 分类管理
                                    put(
                                            "分类管理",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("列表");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .RESOURCE_CATEGORY_MENU);
                                                    }
                                                },
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
                                    // 资源管理
                                    put(
                                            "资源管理",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("列表");
                                                        setSlug(BPermissionConstant.RESOURCE_MENU);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("资源上传");
                                                        setSlug(BPermissionConstant.UPLOAD);
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
                                                new AdminPermission() {
                                                    {
                                                        setSort(40);
                                                        setName("学习进度-查看");
                                                        setSlug(BPermissionConstant.USER_LEARN);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(50);
                                                        setName("学习进度-记录删除");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .USER_LEARN_DESTROY);
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
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("查看部门学员学习进度");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .DEPARTMENT_USER_LEARN);
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
                                                        setName("列表");
                                                        setSlug(BPermissionConstant.COURSE);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(5);
                                                        setName("新增|编辑|删除");
                                                        setSlug(BPermissionConstant.COURSE_CUD);
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
                                    // 系统配置
                                    put(
                                            "系统",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("系统配置");
                                                        setSlug(BPermissionConstant.SYSTEM_CONFIG);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(10);
                                                        setName("管理员日志");
                                                        setSlug(BPermissionConstant.ADMIN_LOG);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(15);
                                                        setName("管理员角色");
                                                        setSlug(BPermissionConstant.ADMIN_ROLE);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(20);
                                                        setName("管理员-列表");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .ADMIN_USER_INDEX);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(25);
                                                        setName("管理员-新增|编辑|删除");
                                                        setSlug(BPermissionConstant.ADMIN_USER_CUD);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(30);
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
                                            });
                                }
                            });
                }
            };

    @Autowired private AdminPermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        HashMap<String, Integer> slugs = permissionService.allSlugs();
        List<AdminPermission> list = new ArrayList<>();
        Date now = new Date();

        permissions.forEach(
                (typeValue, group) -> {
                    group.forEach(
                            (groupNameValue, item) -> {
                                for (AdminPermission permissionItem : item) {
                                    AdminPermission newPermissionItem =
                                            new AdminPermission() {
                                                {
                                                    setType(typeValue);
                                                    setGroupName(groupNameValue);
                                                    setSort(permissionItem.getSort());
                                                    setName(permissionItem.getName());
                                                }
                                            };

                                    Integer existsId = slugs.get(permissionItem.getSlug());
                                    if (existsId != null && existsId > 0) {
                                        newPermissionItem.setId(existsId);
                                        permissionService.updateById(newPermissionItem);
                                        continue;
                                    }

                                    // 不存在
                                    newPermissionItem.setCreatedAt(now);
                                    newPermissionItem.setSlug(permissionItem.getSlug());
                                    list.add(newPermissionItem);
                                }
                            });
                });

        if (!list.isEmpty()) {
            permissionService.saveBatch(list);
        }
    }
}
