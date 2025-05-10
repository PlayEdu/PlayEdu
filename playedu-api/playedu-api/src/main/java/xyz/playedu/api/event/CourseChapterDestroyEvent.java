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
package xyz.playedu.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/26 17:42
 */
@Getter
@Setter
public class CourseChapterDestroyEvent extends ApplicationEvent {
    private Integer adminId;
    private Integer courseId;
    private Integer chapterId;
    private Date createdAt;

    public CourseChapterDestroyEvent(
            Object source, Integer adminId, Integer courseId, Integer chapterId) {
        super(source);
        this.adminId = adminId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.createdAt = new Date();
    }
}
