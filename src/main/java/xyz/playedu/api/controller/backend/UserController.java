package xyz.playedu.api.controller.backend;

import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.types.JsonResponse;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 09:48
 */
@RestController
@RequestMapping("/backend/v1/user")
public class UserController {

    @GetMapping("/index")
    public JsonResponse index() {
        return null;
    }

    @GetMapping("/create")
    public JsonResponse create() {
        return null;
    }

    @PostMapping("/create")
    public JsonResponse store() {
        return null;
    }

    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) {
        return null;
    }

    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "id") Integer id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        return null;
    }

}
