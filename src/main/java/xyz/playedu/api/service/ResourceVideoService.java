/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.ResourceVideo;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service
 * @createDate 2023-03-02 15:13:03
 */
public interface ResourceVideoService extends IService<ResourceVideo> {

    void create(Integer resourceId, Integer duration, String poster);

    void removeByRid(Integer resourceId);

    List<ResourceVideo> chunksByRids(List<Integer> resourceIds);
}
