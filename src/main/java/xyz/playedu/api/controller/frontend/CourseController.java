package xyz.playedu.api.controller.frontend;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 16:25
 */
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);

        CoursePaginateFiler filer = new CoursePaginateFiler();
        filer.setIsShow(1);

        PaginationResult<Course> result = courseService.paginate(page, size, filer);

        return JsonResponse.data(result);
    }

    @GetMapping("/{id}")
    public JsonResponse detail(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Course course = courseService.findOrFail(id);
        if (course.getIsShow().equals(0)) {
            throw new NotFoundException("课程不存在");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);

        return JsonResponse.data(data);
    }

}
