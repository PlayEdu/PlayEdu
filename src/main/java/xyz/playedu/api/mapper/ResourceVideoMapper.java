package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.ResourceVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【resource_videos】的数据库操作Mapper
* @createDate 2023-03-08 13:39:06
* @Entity xyz.playedu.api.domain.ResourceVideo
*/
@Mapper
public interface ResourceVideoMapper extends BaseMapper<ResourceVideo> {

}




