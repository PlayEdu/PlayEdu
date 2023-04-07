/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.CourseChapter;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.CourseChapterMapper;
import xyz.playedu.api.service.CourseChapterService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
