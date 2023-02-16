package xyz.playedu.api.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.middleware.AuthMiddleware;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.PaginationResult;
import xyz.playedu.api.types.JsonResponse;

@RestController
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @AuthMiddleware(prv = SystemConstant.JWT_PRV_ADMIN_USER)
    @GetMapping("/admin/user/index")
    public JsonResponse List(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, null);
        return JsonResponse.data(result);
    }

}
