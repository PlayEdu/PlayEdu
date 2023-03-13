package xyz.playedu.api.controller.frontend;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.request.frontend.ChangePasswordRequest;
import xyz.playedu.api.types.JsonResponse;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:21
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/detail")
    public JsonResponse detail() {
        return JsonResponse.data(null);
    }

    @PutMapping("/password")
    public JsonResponse changePassword(@RequestBody @Validated ChangePasswordRequest req) {
        return JsonResponse.success();
    }


}