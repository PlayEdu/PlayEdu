/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.ResourceCategoryRelation;
import xyz.playedu.api.mapper.ResourceCategoryRelationMapper;
import xyz.playedu.api.service.internal.ResourceCategoryRelationService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_category】的数据库操作Service实现
 * @createDate 2023-03-08 16:54:56
 */
@Service
public class ResourceCategoryRelationServiceImpl
        extends ServiceImpl<ResourceCategoryRelationMapper, ResourceCategoryRelation>
        implements ResourceCategoryRelationService {
    @Override
    public List<Integer> getRidsByCids(List<Integer> categoryIds) {
        List<ResourceCategoryRelation> relations =
                list(query().getWrapper().in("cid", categoryIds));
        if (relations == null) {
            return null;
        }
        return relations.stream().map(ResourceCategoryRelation::getRid).toList();
    }
}
