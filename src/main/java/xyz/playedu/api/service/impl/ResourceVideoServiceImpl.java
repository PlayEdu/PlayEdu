package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.mapper.ResourceVideoMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author tengteng
* @description 针对表【resource_videos】的数据库操作Service实现
* @createDate 2023-03-02 15:13:03
*/
@Service
public class ResourceVideoServiceImpl extends ServiceImpl<ResourceVideoMapper, ResourceVideo>
    implements ResourceVideoService{
    @Override
    public void create(Integer resourceId, Integer duration) {
        ResourceVideo video = new ResourceVideo();
        video.setRid(resourceId);
        video.setDuration(duration);
        video.setCreatedAt(new Date());
        save(video);
    }
}




