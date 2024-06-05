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
package xyz.playedu.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import xyz.playedu.common.domain.LdapDepartment;

/**
 * @author tengyongzhi
 * @description 针对表【ldap_department】的数据库操作Mapper
 * @createDate 2023-11-13 15:46:06 @Entity xyz.playedu.common.domain.LdapDepartment
 */
public interface LdapDepartmentMapper extends BaseMapper<LdapDepartment> {}
