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
package xyz.playedu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.course.domain.CourseAttachment;

import java.util.List;

public interface CourseAttachmentService extends IService<CourseAttachment> {

    CourseAttachment findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void update(CourseAttachment courseAttachment, Integer sort, String title);

    List<CourseAttachment> getAttachmentsByCourseId(Integer courseId);

    CourseAttachment create(Integer courseId, Integer sort, String title, String type, Integer rid);

    Integer getCountByCourseId(Integer courseId);

    void remove(Integer courseId);

    void updateSort(List<Integer> ids, Integer cid);

    List<Integer> getRidsByCourseId(Integer courseId);

    List<CourseAttachment> chunk(List<Integer> attachmentIds);
}
