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
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.UserCourseHourRecord;
import xyz.playedu.api.event.UserCourseHourFinishedEvent;
import xyz.playedu.api.mapper.UserCourseHourRecordMapper;
import xyz.playedu.api.service.UserCourseHourRecordService;
import xyz.playedu.api.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.playedu.api.types.mapper.UserCourseHourRecordUserCountMapper;
import xyz.playedu.api.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserCourseHourRecordPaginateFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Service实现
 * @createDate 2023-03-20 16:41:08
 */
@Service
public class UserCourseHourRecordServiceImpl
        extends ServiceImpl<UserCourseHourRecordMapper, UserCourseHourRecord>
        implements UserCourseHourRecordService {

    @Autowired private ApplicationContext ctx;

    @Override
    public UserCourseHourRecord find(Integer userId, Integer courseId, Integer hourId) {
        return getOne(
                query().getWrapper()
                        .eq("user_id", userId)
                        .eq("course_id", courseId)
                        .eq("hour_id", hourId));
    }

    @Override
    public void storeOrUpdate(
            Integer userId,
            Integer courseId,
            Integer hourId,
            Integer duration,
            Integer totalDuration) {
        UserCourseHourRecord record = find(userId, courseId, hourId);

        // 记录存在 && 已看完 => 跳过处理
        if (record != null && record.getIsFinished() == 1) {
            return;
        }

        // 是否看完
        boolean isFinished = duration >= totalDuration;
        Date finishedAt = isFinished ? new Date() : null;

        if (record == null) {
            UserCourseHourRecord insertRecord = new UserCourseHourRecord();
            insertRecord.setUserId(userId);
            insertRecord.setCourseId(courseId);
            insertRecord.setHourId(hourId);
            insertRecord.setTotalDuration(totalDuration);
            insertRecord.setFinishedDuration(duration);
            insertRecord.setIsFinished(isFinished ? 1 : 0);
            insertRecord.setFinishedAt(finishedAt);
            insertRecord.setCreatedAt(new Date());
            insertRecord.setUpdatedAt(new Date());

            save(insertRecord);
        } else if (record.getFinishedDuration() < duration) {
            UserCourseHourRecord updateRecord = new UserCourseHourRecord();
            updateRecord.setId(record.getId());
            updateRecord.setTotalDuration(totalDuration);
            updateRecord.setFinishedDuration(duration);
            updateRecord.setIsFinished(isFinished ? 1 : 0);
            updateRecord.setFinishedAt(finishedAt);

            updateById(updateRecord);
        }

        if (isFinished) {
            ctx.publishEvent(new UserCourseHourFinishedEvent(this, userId, courseId, hourId));
        }
    }

    @Override
    public Integer getFinishedHourCount(Integer userId, Integer courseId) {
        return Math.toIntExact(
                count(
                        query().getWrapper()
                                .eq("user_id", userId)
                                .eq("course_id", courseId)
                                .eq("is_finished", 1)));
    }

    @Override
    public List<UserCourseHourRecord> getRecords(Integer userId, Integer courseId) {
        return list(query().getWrapper().eq("user_id", userId).eq("course_id", courseId));
    }

    @Override
    public List<UserCourseHourRecord> getLatestCourseIds(Integer userId, Integer size) {
        return getBaseMapper().getUserLatestRecords(userId, size);
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }

    @Override
    public List<UserCourseHourRecordCourseCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished) {
        if (courseIds == null || courseIds.size() == 0) {
            return new ArrayList<>();
        }
        return getBaseMapper().getUserCourseHourCount(userId, courseIds, isFinished);
    }

    @Override
    public List<UserCourseHourRecordUserCountMapper> getUserCourseHourUserCount(
            Integer courseId, List<Integer> userIds, Integer isFinished) {
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        return getBaseMapper().getUserCourseHourUserCount(courseId, userIds, isFinished);
    }

    @Override
    public void remove(Integer userId, Integer courseId) {
        remove(query().getWrapper().eq("user_id", userId).eq("course_id", courseId));
    }

    @Override
    public void remove(Integer userId) {
        remove(query().getWrapper().eq("user_id", userId));
    }

    @Override
    public PaginationResult<UserCourseHourRecord> paginate(
            int page, int size, UserCourseHourRecordPaginateFilter filter) {
        int pageStart = (page - 1) * size;
        filter.setPageStart(pageStart);
        filter.setPageSize(size);

        PaginationResult<UserCourseHourRecord> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    public void remove(Integer userId, Integer courseId, Integer hourId) {
        remove(
                query().getWrapper()
                        .eq("user_id", userId)
                        .eq("course_id", courseId)
                        .eq("hour_id", hourId));
    }

    @Override
    public List<UserCourseHourRecordUserFirstCreatedAtMapper> getUserCourseHourUserFirstCreatedAt(
            Integer courseId, List<Integer> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        return getBaseMapper().getUserCourseHourUserFirstCreatedAt(courseId, userIds);
    }
}
