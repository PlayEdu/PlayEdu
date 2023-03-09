package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.mapper.ResourceVideoMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service实现
 * @createDate 2023-03-02 15:13:03
 */
@Service
public class ResourceVideoServiceImpl extends ServiceImpl<ResourceVideoMapper, ResourceVideo> implements ResourceVideoService {
    @Override
    public void create(Integer resourceId, Integer duration, String poster) {
        ResourceVideo video = new ResourceVideo();
        video.setRid(resourceId);
        video.setDuration(duration);
        video.setPoster(poster);
        video.setCreatedAt(new Date());
        save(video);
    }

    @Override
    public void removeByRid(Integer resourceId) {
        remove(query().getWrapper().eq("rid", resourceId));
    }

    @Override
    public List<ResourceVideo> chunksByRids(List<Integer> resourceIds) {
        if (resourceIds == null || resourceIds.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("rid", resourceIds));
    }
}




