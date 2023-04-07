/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.controller.frontend;

import lombok.SneakyThrows;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.FCtx;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.domain.UserCourseHourRecord;
import xyz.playedu.api.service.*;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/13 16:25
 */
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired private CourseService courseService;

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String categoryIds = MapUtils.getString(params, "category_ids");

        CoursePaginateFiler filer = new CoursePaginateFiler();
        filer.setIsShow(1);
        filer.setCategoryIds(categoryIds);

        PaginationResult<Course> result = courseService.paginate(page, size, filer);

        return JsonResponse.data(result);
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public JsonResponse detail(@PathVariable(name = "id") Integer id) {
        Course course = courseService.findOrFail(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);
        data.put("chapters", chapterService.getChaptersByCourseId(course.getId()));
        data.put(
                "hours",
                hourService.getHoursByCourseId(course.getId()).stream()
                        .collect(Collectors.groupingBy(CourseHour::getChapterId)));
        data.put("learn_record", userCourseRecordService.find(FCtx.getId(), course.getId()));
        data.put(
                "learn_hour_records",
                userCourseHourRecordService.getRecords(FCtx.getId(), course.getId()).stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getHourId, e -> e)));

        return JsonResponse.data(data);
    }
}
