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
package xyz.playedu.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.playedu.course.domain.CourseDepartmentUser;
import xyz.playedu.course.mapper.CourseDepartmentUserMapper;
import xyz.playedu.course.service.CourseDepartmentUserService;

/**
 * @author tengteng
 * @description 针对表【course_department_user】的数据库操作Service实现
 * @createDate 2023-02-24 14:53:52
 */
@Service
public class CourseDepartmentUserServiceImpl
        extends ServiceImpl<CourseDepartmentUserMapper, CourseDepartmentUser>
        implements CourseDepartmentUserService {
    @Override
    public List<Integer> getCourseIdsByDepIds(List<Integer> depIds) {
        return list(query().getWrapper().in("range_id", depIds)).stream()
                .map(CourseDepartmentUser::getCourseId)
                .toList();
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseDepartmentUser::getRangeId)
                .toList();
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }

    @Override
    public List<Integer> getCourseIdsByDepId(Integer depId) {
        return list(query().getWrapper().eq("range_id", depId)).stream()
                .map(CourseDepartmentUser::getCourseId)
                .toList();
    }
}
