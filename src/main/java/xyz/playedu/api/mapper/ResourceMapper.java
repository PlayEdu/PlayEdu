package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【resources】的数据库操作Mapper
* @createDate 2023-03-13 10:25:30
* @Entity xyz.playedu.api.domain.Resource
*/
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

}




