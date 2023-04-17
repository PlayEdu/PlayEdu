/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.CourseDepartment;
import xyz.playedu.api.mapper.CourseDepartmentMapper;
import xyz.playedu.api.service.CourseDepartmentService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_department】的数据库操作Service实现
 * @createDate 2023-02-24 14:53:52
 */
@Service
public class CourseDepartmentServiceImpl
        extends ServiceImpl<CourseDepartmentMapper, CourseDepartment>
        implements CourseDepartmentService {
    @Override
    public List<Integer> getCourseIdsByDepIds(List<Integer> depIds) {
        return list(query().getWrapper().in("dep_id", depIds)).stream()
                .map(CourseDepartment::getCourseId)
                .toList();
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseDepartment::getDepId)
                .toList();
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }
}
