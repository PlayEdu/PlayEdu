/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.mapper.ResourceVideoMapper;
import xyz.playedu.api.service.ResourceVideoService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service实现
 * @createDate 2023-03-02 15:13:03
 */
@Service
public class ResourceVideoServiceImpl extends ServiceImpl<ResourceVideoMapper, ResourceVideo>
        implements ResourceVideoService {
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
