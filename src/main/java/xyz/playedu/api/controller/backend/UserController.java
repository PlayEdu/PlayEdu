package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.event.UserDestroyEvent;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.UserRequest;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;
import xyz.playedu.api.util.HelperUtil;

import java.util.Date;
import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 09:48
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_INDEX)
    @GetMapping("/index")
    public JsonResponse index(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "id_card", required = false) String idCard,
            @RequestParam(name = "is_active", required = false) Integer isActive,
            @RequestParam(name = "is_lock", required = false) Integer isLock,
            @RequestParam(name = "is_verify", required = false) Integer isVerify,
            @RequestParam(name = "is_set_password", required = false) Integer isSetPassword,
            @RequestParam(name = "created_at", required = false) Date[] createdAt
    ) {
        UserPaginateFilter filter = new UserPaginateFilter();
        if (name != null && name.length() > 0) {
            filter.setName(name);
        }
        if (nickname != null && nickname.length() > 0) {
            filter.setNickname(nickname);
        }
        if (email != null && email.length() > 0) {
            filter.setEmail(email);
        }
        if (idCard != null && idCard.length() > 0) {
            filter.setIdCard(idCard);
        }
        if (isActive != null) {
            filter.setIsActive(isActive);
        }
        if (isLock != null) {
            filter.setIsLock(isLock);
        }
        if (isVerify != null) {
            filter.setIsVerify(isVerify);
        }
        if (isSetPassword != null) {
            filter.setIsSetPassword(isSetPassword);
        }
        if (createdAt != null && createdAt.length == 2) {
            filter.setCreatedAt(createdAt);
        }

        PaginationResult<User> result = userService.paginate(page, size, filter);
        return JsonResponse.data(result);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_STORE)
    @GetMapping("/create")
    public JsonResponse create() {
        return JsonResponse.data(null);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_STORE)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated UserRequest request) {
        if (userService.emailIsExists(request.getEmail())) {
            return JsonResponse.error("邮箱已存在");
        }

        String salt = HelperUtil.randomString(6);
        String password = HelperUtil.MD5(request.getPassword() + salt);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setName(request.getName());
        user.setAvatar(request.getAvatar());
        user.setPassword(password);
        user.setSalt(salt);
        user.setIdCard(request.getIdCard());
        user.setCredit1(request.getCredit1());
        user.setIsActive(request.getIsActive());
        user.setIsLock(request.getIsLock());
        user.setIsVerify(request.getIsVerify());
        user.setVerifyAt(request.getVerifyAt());
        user.setIsSetPassword(request.getIsSetPassword());

        user.setCreateIp("127.0.0.1");
        user.setCreateCity("内网");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        userService.save(user);

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_UPDATE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return JsonResponse.error("学员不存在");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_UPDATE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated UserRequest request) {
        User user = userService.getById(id);
        if (user == null) {
            return JsonResponse.error("学员不存在");
        }

        if (!request.getEmail().equals(user.getEmail()) && userService.emailIsExists(request.getEmail())) {
            return JsonResponse.error("邮箱已存在");
        }

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setEmail(request.getEmail());
        newUser.setNickname(request.getNickname());
        newUser.setName(request.getName());
        newUser.setAvatar(request.getAvatar());
        newUser.setIdCard(request.getIdCard());
        newUser.setCredit1(request.getCredit1());
        newUser.setIsActive(request.getIsActive());
        newUser.setIsLock(request.getIsLock());
        newUser.setIsVerify(request.getIsVerify());
        newUser.setVerifyAt(request.getVerifyAt());
        newUser.setIsSetPassword(request.getIsSetPassword());

        if (request.getPassword() != null && request.getPassword().length() > 0) {//更新密码
            String salt = HelperUtil.randomString(6);
            String password = HelperUtil.MD5(request.getPassword());

            newUser.setPassword(password);
            newUser.setSalt(salt);
        }

        userService.updateById(newUser);

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_DESTROY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return JsonResponse.error("学员不存在");
        }
        userService.removeById(user.getId());

        applicationContext.publishEvent(new UserDestroyEvent(this, user.getId(), new Date()));

        return JsonResponse.success();
    }

}
