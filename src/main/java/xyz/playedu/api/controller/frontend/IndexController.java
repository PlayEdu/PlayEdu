/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.controller.frontend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.api.types.JsonResponse;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/24 17:42
 */
@RestController
public class IndexController {
    @GetMapping("/")
    public JsonResponse index() {
        return JsonResponse.success();
    }
}
