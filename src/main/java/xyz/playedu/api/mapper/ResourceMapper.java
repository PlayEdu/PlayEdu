package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.playedu.api.types.mapper.ResourceCategoryCountMapper;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Mapper
 * @createDate 2023-03-13 10:25:30
 * @Entity xyz.playedu.api.domain.Resource
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> paginate(ResourcePaginateFilter filter);

    Long paginateCount(ResourcePaginateFilter filter);

}





