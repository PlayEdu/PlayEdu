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
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.User;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【users】的数据库操作Service
 * @createDate 2023-02-23 13:50:58
 */
public interface UserService extends IService<User> {
    boolean emailIsExists(String email);

    PaginationResult<User> paginate(int page, int size, UserPaginateFilter filter);

    List<String> existsEmailsByEmails(List<String> emails);

    void removeRelateDepartmentsByUserId(Integer userId);

    User findOrFail(Integer id) throws NotFoundException;

    User find(Integer id);

    User find(String email);

    User createWithDepIds(
            String email,
            String name,
            String avatar,
            String password,
            String idCard,
            Integer[] depIds);

    User updateWithDepIds(
            User user,
            String email,
            String name,
            String avatar,
            String password,
            String idCard,
            Integer[] depIds);

    List<Integer> getDepIdsByUserId(Integer userId);

    void passwordChange(User user, String oldPassword, String newPassword) throws ServiceException;

    List<User> chunks(List<Integer> ids, List<String> fields);

    List<User> chunks(List<Integer> ids);

    Long total();

    Long todayCount();

    Long yesterdayCount();

    Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> userIds);

    void changeAvatar(Integer userId, String avatar);
}
