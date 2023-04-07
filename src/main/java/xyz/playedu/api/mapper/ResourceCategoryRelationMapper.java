/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.ResourceCategoryRelation;

/**
 * @author tengteng
 * @description 针对表【resource_category】的数据库操作Mapper
 * @createDate 2023-03-08 16:54:56 @Entity xyz.playedu.api.domain.ResourceCategoryRelation
 */
@Mapper
public interface ResourceCategoryRelationMapper extends BaseMapper<ResourceCategoryRelation> {}
