package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.PlayEduBContext;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.ImageCaptchaResult;
import xyz.playedu.api.types.JsonResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/backend/v1/system")
public class SystemController {

    @Autowired
    private ImageCaptchaService imageCaptchaService;

    @GetMapping("/image-captcha")
    public JsonResponse imageCaptcha() throws IOException {
        ImageCaptchaResult imageCaptchaResult = imageCaptchaService.generate();

        HashMap<String, String> data = new HashMap<>();
        data.put("key", imageCaptchaResult.getKey());
        data.put("image", imageCaptchaResult.getImage());

        return JsonResponse.data(data);
    }

    @GetMapping("/config")
    public JsonResponse config() {
        Map<String, String> data = PlayEduBContext.getConfig();
        return JsonResponse.data(data);
    }

}
