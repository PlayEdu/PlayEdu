/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.controller.backend;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.event.UserCourseRecordDestroyEvent;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseUserDestroyRequest;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CourseUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/24 16:08
 */
@RestController
@RequestMapping("/backend/v1/course/{courseId}/user")
public class CourseUserController {

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserService userService;

    @Autowired private ApplicationContext ctx;

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_USER)
    @GetMapping("/index")
    public JsonResponse index(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");

        CourseUserPaginateFilter filter = new CourseUserPaginateFilter();
        filter.setCourseId(courseId);
        filter.setName(name);
        filter.setEmail(email);
        filter.setIdCard(idCard);
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);

        PaginationResult<UserCourseRecord> result =
                userCourseRecordService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put(
                "users",
                userService.chunks(
                        result.getData().stream().map(UserCourseRecord::getUserId).toList()));

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_USER_DESTROY)
    @PostMapping("/destroy")
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseUserDestroyRequest req) {
        if (req.getIds().size() == 0) {
            return JsonResponse.error("请选择需要删除的数据");
        }
        List<UserCourseRecord> records =
                userCourseRecordService.chunks(
                        req.getIds(),
                        new ArrayList<>() {
                            {
                                add("user_id");
                                add("id");
                            }
                        });
        for (UserCourseRecord record : records) {
            userCourseRecordService.removeById(record);
            ctx.publishEvent(new UserCourseRecordDestroyEvent(this, record.getUserId(), courseId));
        }
        return JsonResponse.success();
    }
}
