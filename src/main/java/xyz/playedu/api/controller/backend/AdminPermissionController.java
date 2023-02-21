package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.types.JsonResponse;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/20 14:19
 */
@RestController
@RequestMapping("/backend/v1/admin-permission")
public class AdminPermissionController {

    @Autowired
    private AdminPermissionService adminPermissionService;

    @GetMapping("/index")
    public JsonResponse index() {
        List<AdminPermission> data = adminPermissionService.list();
        return JsonResponse.data(data);
    }

}
