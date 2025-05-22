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
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import xyz.playedu.course.domain.UserLearnDurationRecord;
import xyz.playedu.course.mapper.UserLearnDurationRecordMapper;
import xyz.playedu.course.service.UserLearnDurationRecordService;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Service实现
 * @createDate 2023-03-20 16:41:12
 */
@Service
public class UserLearnDurationRecordServiceImpl
        extends ServiceImpl<UserLearnDurationRecordMapper, UserLearnDurationRecord>
        implements UserLearnDurationRecordService {

    @Override
    @SneakyThrows
    public void store(
            Integer userId, String fromId, String fromScene, Long startTime, Long endTime) {
        // 处理日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(endTime));

        UserLearnDurationRecord record = new UserLearnDurationRecord();
        record.setUserId(userId);
        record.setFromId(fromId);
        record.setFromScene(fromScene);
        record.setStartAt(new Date(startTime));
        record.setEndAt(new Date(endTime));
        record.setDuration((int) (endTime - startTime));
        record.setCreatedDate(simpleDateFormat.parse(date));

        save(record);
    }

    @Override
    public void remove(Integer userId) {
        remove(query().getWrapper().eq("user_id", userId));
    }
}
