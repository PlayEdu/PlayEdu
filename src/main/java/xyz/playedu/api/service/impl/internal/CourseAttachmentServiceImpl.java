/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.CourseAttachment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.CourseAttachmentMapper;
import xyz.playedu.api.service.CourseAttachmentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseAttachmentServiceImpl
        extends ServiceImpl<CourseAttachmentMapper, CourseAttachment>
        implements CourseAttachmentService {

    @Override
    public CourseAttachment findOrFail(Integer id, Integer courseId) throws NotFoundException {
        CourseAttachment attachment =
                getOne(query().getWrapper().eq("id", id).eq("course_id", courseId));
        if (attachment == null) {
            throw new NotFoundException("附件不存在");
        }
        return attachment;
    }

    @Override
    public void update(CourseAttachment courseAttachment, Integer sort, String title) {
        CourseAttachment attachment = new CourseAttachment();
        attachment.setId(courseAttachment.getId());
        attachment.setSort(sort);
        attachment.setTitle(title);

        updateById(attachment);
    }

    @Override
    public List<CourseAttachment> getAttachmentsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId).orderByAsc("sort"));
    }

    @Override
    public CourseAttachment create(
            Integer courseId, Integer sort, String title, String type, Integer rid) {
        CourseAttachment attachment = new CourseAttachment();
        attachment.setCourseId(courseId);
        attachment.setSort(sort);
        attachment.setTitle(title);
        attachment.setType(type);
        attachment.setRid(rid);
        attachment.setCreatedAt(new Date());

        save(attachment);

        return attachment;
    }

    @Override
    public Integer getCountByCourseId(Integer courseId) {
        return Math.toIntExact(count(query().getWrapper().eq("course_id", courseId)));
    }

    @Override
    public void remove(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }

    @Override
    public void updateSort(List<Integer> ids, Integer cid) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<CourseAttachment> attachments = new ArrayList<>();
        final Integer[] sortVal = {0};
        for (Integer idVal : ids) {
            attachments.add(
                    new CourseAttachment() {
                        {
                            setId(idVal);
                            setCourseId(cid);
                            setSort(sortVal[0]++);
                        }
                    });
        }
        updateBatchById(attachments);
    }

    @Override
    public List<Integer> getRidsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseAttachment::getRid)
                .toList();
    }

    @Override
    public List<CourseAttachment> chunk(List<Integer> attachmentIds) {
        if (attachmentIds == null || attachmentIds.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", attachmentIds));
    }
}
