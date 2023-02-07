package xyz.playedu.api.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.impl.AdminUserServiceImpl;
import xyz.playedu.api.types.PaginationResult;
import xyz.playedu.api.types.JsonResponse;

@RestController
public class AdminUserController {

    @Autowired
    private AdminUserServiceImpl adminUserService;

    @GetMapping("/admin/user/index")
    public JsonResponse<Object> List(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, null);
        return JsonResponse.data(result);
    }

    @GetMapping("/admmin/user/test")
    public void TestException() throws ServiceException {
        throw new RuntimeException("我是错误");
    }

}
