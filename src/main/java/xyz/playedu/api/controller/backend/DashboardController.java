package xyz.playedu.api.controller.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/7 13:55
 */
@RestController
@RequestMapping("/backend/v1/dashboard")
public class DashboardController {

    @GetMapping("/index")
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("version", SystemConstant.VERSION);

        return JsonResponse.data(data);
    }

}
