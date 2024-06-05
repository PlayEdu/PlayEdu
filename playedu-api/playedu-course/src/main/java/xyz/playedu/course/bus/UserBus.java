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
package xyz.playedu.course.bus;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.common.domain.Department;
import xyz.playedu.common.service.DepartmentService;
import xyz.playedu.common.service.UserService;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.course.service.CourseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 14:56
 */
@Component
public class UserBus {

    @Autowired private CourseService courseService;

    @Autowired private UserService userService;

    @Autowired private DepartmentService departmentService;

    public boolean canSeeCourse(Integer userId, Integer courseId) {
        List<Integer> courseDepIds = courseService.getDepIdsByCourseId(courseId);
        if (StringUtil.isEmpty(courseDepIds)) {
            // 线上课全部部门=>任何学员都可以学习
            return true;
        }

        // 获取学员所属部门以及所有父级部门
        List<Integer> allDepIds = new ArrayList<>();
        List<Integer> userDepIds = userService.getDepIdsByUserId(userId);
        if (StringUtil.isNotEmpty(userDepIds)) {
            List<Department> departmentList = departmentService.chunk(userDepIds);
            if (StringUtil.isNotEmpty(departmentList)) {
                for (Department dep : departmentList) {
                    allDepIds.add(dep.getId());
                    if (StringUtil.isNotEmpty(dep.getParentChain())) {
                        allDepIds.addAll(
                                Arrays.stream(dep.getParentChain().split(","))
                                        .map(Integer::valueOf)
                                        .toList());
                    }
                }
            }
        }

        if (StringUtil.isEmpty(allDepIds)) {
            return false;
        }
        return CollectionUtils.intersection(courseDepIds, allDepIds).size() > 0;
    }
}
