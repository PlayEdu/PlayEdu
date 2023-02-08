package xyz.playedu.api.controller.admin;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.request.LoginRequest;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/v1/auth")
public class LoginController {

    @PostMapping("/login")
    public JsonResponse<Object> login(@RequestBody @Validated LoginRequest loginRequest) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", loginRequest.email);
        map.put("password", loginRequest.password);
        return JsonResponse.data(map);
    }

    @PostMapping("/logout")
    public JsonResponse<String> logout() {
        return JsonResponse.success("success");
    }

}
