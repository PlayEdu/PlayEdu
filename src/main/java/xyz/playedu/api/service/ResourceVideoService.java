package xyz.playedu.api.service;

import xyz.playedu.api.domain.ResourceVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tengteng
* @description 针对表【resource_videos】的数据库操作Service
* @createDate 2023-03-02 15:13:03
*/
public interface ResourceVideoService extends IService<ResourceVideo> {

    void create(Integer resourceId, Integer duration);
}
