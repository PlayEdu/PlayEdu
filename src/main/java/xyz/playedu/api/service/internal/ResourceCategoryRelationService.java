/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.internal;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.ResourceCategoryRelation;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_category】的数据库操作Service
 * @createDate 2023-03-08 16:54:56
 */
public interface ResourceCategoryRelationService extends IService<ResourceCategoryRelation> {

    List<Integer> getRidsByCids(List<Integer> categoryIds);
}
