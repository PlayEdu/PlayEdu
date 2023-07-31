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
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Mapper
 * @createDate 2023-03-13 10:25:30 @Entity xyz.playedu.api.domain.Resource
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> paginate(ResourcePaginateFilter filter);

    Long paginateCount(ResourcePaginateFilter filter);

    List<String> paginateType(ResourcePaginateFilter filter);
}
