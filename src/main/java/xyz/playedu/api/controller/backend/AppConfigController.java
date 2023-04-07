/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.request.backend.AppConfigRequest;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.types.JsonResponse;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/9 11:14
 */
@RestController
@RequestMapping("/backend/v1/app-config")
public class AppConfigController {

    @Autowired private AppConfigService configService;

    @GetMapping("")
    public JsonResponse index() {
        List<AppConfig> configs = configService.allShow();
        return JsonResponse.data(configs);
    }

    @PutMapping("")
    public JsonResponse save(@RequestBody AppConfigRequest req) {
        configService.saveFromMap(req.getData());
        return JsonResponse.data(null);
    }
}
