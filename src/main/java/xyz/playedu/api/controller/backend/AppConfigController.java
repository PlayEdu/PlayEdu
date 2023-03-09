package xyz.playedu.api.controller.backend;

import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.types.JsonResponse;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/9 11:14
 */
@RestController
@RequestMapping("/backend/v1/app-config")
public class AppConfigController {

    @GetMapping("/index")
    public JsonResponse index() {
        return JsonResponse.data(null);
    }

    @PutMapping("/index")
    public JsonResponse save() {
        return JsonResponse.data(null);
    }

}
