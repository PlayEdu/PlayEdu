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
package xyz.playedu.common.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.common.domain.Department;
import xyz.playedu.common.exception.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Service
 * @createDate 2023-02-19 10:39:57
 */
public interface DepartmentService extends IService<Department> {

    List<Department> listByParentId(Integer id);

    List<Department> all();

    Department findOrFail(Integer id) throws NotFoundException;

    void destroy(Integer id) throws NotFoundException;

    void update(Department department, String name, Integer parentId, Integer sort)
            throws NotFoundException;

    String compParentChain(Integer parentId) throws NotFoundException;

    String childrenParentChain(Department department);

    Integer create(String name, Integer parentId, Integer sort) throws NotFoundException;

    void remoteRelateUsersByDepId(Integer depId);

    List<Integer> getUserIdsByDepId(Integer depId);

    void changeParent(Integer id, Integer parentId, List<Integer> ids) throws NotFoundException;

    void resetSort(List<Integer> ids);

    Map<Integer, List<Department>> groupByParent();

    Map<Integer, String> id2name();

    Long total();

    Map<Integer, Integer> getDepartmentsUserCount();

    List<Department> chunk(List<Integer> ids);

    Integer createWithChainList(List<String> ou);

    Department findByName(String name, Integer parentId);

    List<Department> getChildDepartmentsByParentId(Integer parentId);
}
