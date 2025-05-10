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
package xyz.playedu.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.course.domain.CourseChapter;
import xyz.playedu.course.mapper.CourseChapterMapper;
import xyz.playedu.course.service.CourseChapterService;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Service实现
 * @createDate 2023-02-26 17:30:18
 */
@Service
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter>
        implements CourseChapterService {

    @Override
    public void create(Integer courseId, String name, Integer sort) {
        CourseChapter chapter = new CourseChapter();
        chapter.setCourseId(courseId);
        chapter.setName(name);
        chapter.setSort(sort);
        chapter.setCreatedAt(new Date());
        chapter.setUpdatedAt(new Date());
        save(chapter);
    }

    @Override
    public void update(CourseChapter chapter, String name, Integer sort) {
        CourseChapter newChapter = new CourseChapter();
        newChapter.setId(chapter.getId());
        newChapter.setName(name);
        newChapter.setSort(sort);
        updateById(newChapter);
    }

    @Override
    public CourseChapter findOrFail(Integer id) throws NotFoundException {
        CourseChapter chapter = getOne(query().getWrapper().eq("id", id));
        if (chapter == null) {
            throw new NotFoundException("章节不存在");
        }
        return chapter;
    }

    @Override
    public List<CourseChapter> getChaptersByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId).orderByAsc("sort"));
    }

    @Override
    public CourseChapter findOrFail(Integer id, Integer courseId) throws NotFoundException {
        CourseChapter chapter = getOne(query().getWrapper().eq("id", id).eq("course_id", courseId));
        if (chapter == null) {
            throw new NotFoundException("章节不存在");
        }
        return chapter;
    }

    @Override
    public void updateSort(List<Integer> ids, Integer cid) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<CourseChapter> chapters = new ArrayList<>();
        final Integer[] sortVal = {0};
        for (Integer idItem : ids) {
            chapters.add(
                    new CourseChapter() {
                        {
                            setId(idItem);
                            setId(cid);
                            setSort(sortVal[0]++);
                        }
                    });
        }
        updateBatchById(chapters);
    }
}
