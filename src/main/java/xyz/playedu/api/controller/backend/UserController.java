package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.event.UserDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.UserImportRequest;
import xyz.playedu.api.request.backend.UserRequest;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;
import xyz.playedu.api.util.HelperUtil;

import java.util.*;

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
    private UserDepartmentService userDepartmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ApplicationContext context;

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_INDEX)
    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String nickname = MapUtils.getString(params, "nickname");
        String idCard = MapUtils.getString(params, "id_card");
        Integer isActive = MapUtils.getInteger(params, "is_active");
        Integer isLock = MapUtils.getInteger(params, "is_lock");
        Integer isVerify = MapUtils.getInteger(params, "is_verify");
        Integer isSetPassword = MapUtils.getInteger(params, "is_set_password");
        String createdAt = MapUtils.getString(params, "created_at");
        String depIds = MapUtils.getString(params, "dep_ids");

        UserPaginateFilter filter = new UserPaginateFilter(){{
            setName(name);
            setNickname(nickname);
            setEmail(email);
            setIdCard(idCard);
            setIsActive(isActive);
            setIsLock(isLock);
            setIsVerify(isVerify);
            setIsSetPassword(isSetPassword);
            setCreatedAt(createdAt);
            setDepIds(depIds);
            setSortAlgo(sortAlgo);
            setSortField(sortField);
        }};

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
    public JsonResponse store(@RequestBody @Validated UserRequest req) {
        String email = req.getEmail();
        if (userService.emailIsExists(email)) {
            return JsonResponse.error("邮箱已存在");
        }
        String password = req.getPassword();
        if (password.length() == 0) {
            return JsonResponse.error("请输入密码");
        }
        userService.createWithDepIds(email, req.getName(), req.getAvatar(), req.getPassword(), req.getIdCard(), req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_UPDATE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        User user = userService.findOrFail(id);

        List<Integer> depIds = userService.getDepIdsByUserId(user.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("dep_ids", depIds);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_UPDATE)
    @PutMapping("/{id}")
    @Transactional
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated UserRequest req) throws NotFoundException {
        User user = userService.findOrFail(id);

        String email = req.getEmail();
        if (!email.equals(user.getEmail()) && userService.emailIsExists(email)) {
            return JsonResponse.error("邮箱已存在");
        }

        userService.updateWithDepIds(user, email, req.getNickname(), req.getName(), req.getAvatar(), req.getPassword(), req.getIdCard(), req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.USER_DESTROY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        User user = userService.findOrFail(id);
        userService.removeById(user.getId());
        context.publishEvent(new UserDestroyEvent(this, user.getId(), new Date()));
        return JsonResponse.success();
    }

    @PostMapping("/store-batch")
    @Transactional
    public JsonResponse batchStore(@RequestBody @Validated UserImportRequest request) {
        String[][] users = request.getUsers();
        if (users.length == 0) {
            return JsonResponse.error("数据为空");
        }
        if (users.length > 1000) {
            return JsonResponse.error("一次最多导入1000条数据");
        }

        Integer startLine = request.getStartLine();

        List<String[]> errorLines = new ArrayList<>();
        errorLines.add(new String[]{"错误行", "错误信息"});//表头

        // 参数长度校验
        for (int i = 0; i < users.length; i++) {
            if (users[i].length != 6) {
                errorLines.add(new String[]{"第" + (i + startLine) + "行", "参数错误"});
            }
        }
        if (errorLines.size() > 1) {
            return JsonResponse.error("导入数据有误", errorLines);
        }

        // 读取存在的部门
        List<Integer> depIds = departmentService.allIds();

        // 邮箱输入重复检测 || 部门存在检测
        HashMap<String, Integer> emailMap = new HashMap<>();
        HashMap<String, String[]> depMap = new HashMap<>();
        List<String> emails = new ArrayList<>();
        List<User> insertUsers = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            //c0: 部门ids数组
            //c1: 邮箱
            //c2: 昵称
            //c3: 密码
            //c4: 姓名
            //c5: 身份证号

            String tmpEmail = users[i][1];
            if (emailMap.get(tmpEmail) != null) {//存在重复
                errorLines.add(new String[]{"第" + (i + startLine) + "行", "邮箱重复"});
            } else {
                emailMap.put(tmpEmail, i + startLine);
            }

            emails.add(tmpEmail);

            // 部门存在检测
            if (users[i][0] != null && users[i][0].length() > 0) {
                String[] tmpDepIds = users[i][0].split(",");
                for (int j = 0; j < tmpDepIds.length; j++) {
                    if (!depIds.contains(Integer.valueOf(tmpDepIds[j]))) {
                        errorLines.add(new String[]{"第" + (i + startLine) + "行", "部门id[" + tmpDepIds[j] + "]不存在"});
                    }
                }
                depMap.put(users[i][1], tmpDepIds);
            }

            // 昵称为空检测
            if (users[i][2] == null || users[i][2].length() == 0) {
                errorLines.add(new String[]{"第" + (i + startLine) + "行", "昵称为空"});
            }

            // 密码为空检测
            if (users[i][3] == null || users[i][3].length() == 0) {
                errorLines.add(new String[]{"第" + (i + startLine) + "行", "密码为空"});
            }

            // 带插入数据
            User tmpInsertUser = new User();
            String tmpSalt = HelperUtil.randomString(6);
            String tmpPassword = HelperUtil.MD5(users[i][3] + tmpSalt);
            tmpInsertUser.setEmail(users[i][1]);
            tmpInsertUser.setNickname(users[i][2]);
            tmpInsertUser.setPassword(tmpPassword);
            tmpInsertUser.setSalt(tmpSalt);
            tmpInsertUser.setName(users[i][4]);
            tmpInsertUser.setIdCard(users[i][5]);
            tmpInsertUser.setCreateIp(SystemConstant.INTERNAL_IP);
            tmpInsertUser.setCreateCity(SystemConstant.INTERNAL_IP_AREA);
            tmpInsertUser.setCreatedAt(new Date());
            tmpInsertUser.setUpdatedAt(new Date());

            insertUsers.add(tmpInsertUser);
        }

        if (errorLines.size() > 1) {
            return JsonResponse.error("导入数据有误", errorLines);
        }

        // 邮箱是否注册检测
        List<String> existsEmails = userService.existsEmailsByEmails(emails);
        if (existsEmails.size() > 0) {
            for (String tmpEmail : existsEmails) {
                errorLines.add(new String[]{"第" + emailMap.get(tmpEmail) + "行", "邮箱已注册"});
            }
        }
        if (errorLines.size() > 1) {
            return JsonResponse.error("导入数据有误", errorLines);
        }

        userService.saveBatch(insertUsers);

        // 部门关联
        List<UserDepartment> userDepartments = new ArrayList<>();
        for (User tmpUser : insertUsers) {
            String[] tmpDepIds = depMap.get(tmpUser.getEmail());
            if (tmpDepIds != null) {
                for (int i = 0; i < tmpDepIds.length; i++) {
                    UserDepartment tmpUserDep = new UserDepartment();
                    tmpUserDep.setUserId(tmpUser.getId());
                    tmpUserDep.setDepId(Integer.valueOf(tmpDepIds[i]));

                    userDepartments.add(tmpUserDep);
                }
            }
        }
        userDepartmentService.saveBatch(userDepartments);

        return JsonResponse.success();
    }

}
