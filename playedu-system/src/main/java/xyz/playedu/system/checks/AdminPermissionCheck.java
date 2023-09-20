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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.domain.AdminPermission;
import xyz.playedu.common.service.AdminPermissionService;

import java.util.*;

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
                                    // 管理员日志
                                    put(
                                            "管理员日志",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("列表");
                                                        setSlug(BPermissionConstant.ADMIN_LOG);
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
                                                        setName("学员学习");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .DEPARTMENT_USER_LEARN);
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
                                                        setName("学习");
                                                        setSlug(BPermissionConstant.USER_LEARN);
                                                    }
                                                },
                                                new AdminPermission() {
                                                    {
                                                        setSort(50);
                                                        setName("学习-删除");
                                                        setSlug(
                                                                BPermissionConstant
                                                                        .USER_LEARN_DESTROY);
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
                                    // 系统配置
                                    put(
                                            "系统配置",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("系统配置");
                                                        setSlug(BPermissionConstant.SYSTEM_CONFIG);
                                                    }
                                                },
                                            });
                                    // 其它
                                    put(
                                            "其它权限",
                                            new AdminPermission[] {
                                                new AdminPermission() {
                                                    {
                                                        setSort(0);
                                                        setName("文件上传");
                                                        setSlug(BPermissionConstant.UPLOAD);
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
    public void run(String... args) throws Exception {
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

        if (!list.isEmpty()) {
            permissionService.saveBatch(list);
        }
    }
}
