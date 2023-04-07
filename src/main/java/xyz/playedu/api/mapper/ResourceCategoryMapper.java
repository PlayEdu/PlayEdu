/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.ResourceCategory;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Mapper
 * @createDate 2023-03-08 16:50:54 @Entity xyz.playedu.api.domain.ResourceCategory
 */
@Mapper
public interface ResourceCategoryMapper extends BaseMapper<ResourceCategory> {}
