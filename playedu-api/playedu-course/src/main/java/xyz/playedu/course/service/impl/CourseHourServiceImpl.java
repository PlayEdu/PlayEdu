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
import xyz.playedu.course.domain.CourseHour;
import xyz.playedu.course.mapper.CourseHourMapper;
import xyz.playedu.course.service.CourseHourService;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service实现
 * @createDate 2023-03-15 10:16:45
 */
@Service
public class CourseHourServiceImpl extends ServiceImpl<CourseHourMapper, CourseHour>
        implements CourseHourService {

    @Override
    public CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException {
        CourseHour hour = getOne(query().getWrapper().eq("id", id).eq("course_id", courseId));
        if (hour == null) {
            throw new NotFoundException("课时不存在");
        }
        return hour;
    }

    @Override
    public void update(
            CourseHour courseHour,
            Integer chapterId,
            Integer sort,
            String title,
            Integer duration) {
        CourseHour hour = new CourseHour();
        hour.setId(courseHour.getId());
        hour.setChapterId(chapterId);
        hour.setSort(sort);
        hour.setTitle(title);
        hour.setDuration(duration);

        updateById(hour);
    }

    @Override
    public List<CourseHour> getHoursByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId).orderByAsc("sort"));
    }

    @Override
    public CourseHour create(
            Integer courseId,
            Integer chapterId,
            Integer sort,
            String title,
            String type,
            Integer rid,
            Integer duration) {
        CourseHour hour = new CourseHour();
        hour.setCourseId(courseId);
        hour.setChapterId(chapterId);
        hour.setSort(sort);
        hour.setTitle(title);
        hour.setType(type);
        hour.setRid(rid);
        hour.setDuration(duration);
        hour.setCreatedAt(new Date());

        save(hour);

        return hour;
    }

    @Override
    public Integer getCountByCourseId(Integer courseId) {
        return Math.toIntExact(count(query().getWrapper().eq("course_id", courseId)));
    }

    @Override
    public Integer getCountByChapterId(Integer chapterId) {
        return Math.toIntExact(count(query().getWrapper().eq("chapter_id", chapterId)));
    }

    @Override
    public void remove(Integer courseId, Integer chapterId) {
        remove(query().getWrapper().eq("course_id", courseId).eq("chapter_id", chapterId));
    }

    @Override
    public void updateSort(List<Integer> ids, Integer cid) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<CourseHour> hours = new ArrayList<>();
        final Integer[] sortVal = {0};
        for (Integer idVal : ids) {
            hours.add(
                    new CourseHour() {
                        {
                            setId(idVal);
                            setCourseId(cid);
                            setSort(sortVal[0]++);
                        }
                    });
        }
        updateBatchById(hours);
    }

    @Override
    public List<Integer> getRidsByCourseId(Integer courseId, String type) {
        return list(query().getWrapper().eq("course_id", courseId).eq("type", type)).stream()
                .map(CourseHour::getRid)
                .toList();
    }

    @Override
    public List<CourseHour> chunk(List<Integer> hourIds) {
        if (hourIds == null || hourIds.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", hourIds));
    }
}
