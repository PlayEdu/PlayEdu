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
package xyz.playedu.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.common.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.playedu.course.domain.CourseAttachmentDownloadLog;

/**
 * @author tengteng
 * @description 针对表【course_attachment_download_log】的数据库操作Mapper
 * @createDate 2023-08-02 17:34:01
 */
@Mapper
public interface CourseAttachmentDownloadLogMapper extends BaseMapper<CourseAttachmentDownloadLog> {

    List<CourseAttachmentDownloadLog> paginate(CourseAttachmentDownloadLogPaginateFiler filer);

    Long paginateCount(CourseAttachmentDownloadLogPaginateFiler filer);
}
