package xyz.playedu.api.service;

import xyz.playedu.api.domain.ResourceVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service
 * @createDate 2023-03-02 15:13:03
 */
public interface ResourceVideoService extends IService<ResourceVideo> {

    void create(Integer resourceId, Integer duration);

    void removeByRid(Integer resourceId);

    List<ResourceVideo> chunksByResourceIds(List<Integer> resourceIds);
}
