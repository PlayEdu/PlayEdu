/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
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
}
