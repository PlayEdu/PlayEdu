package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.PaginationResult;
import xyz.playedu.api.types.JsonResponse;

@RestController
@Slf4j
@RequestMapping("/backend/v1/admin-user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/index")
    public JsonResponse List(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, null);
        return JsonResponse.data(result);
    }

}
