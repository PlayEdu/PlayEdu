package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.config.MinioConfig;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.ImageCaptchaResult;
import xyz.playedu.api.types.JsonResponse;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/backend/v1/system")
public class SystemController {

    @Autowired
    private ImageCaptchaService imageCaptchaService;

    @Autowired
    private MinioConfig minioConfig;

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
        HashMap<String, Object> data = new HashMap<>();
        data.put("minio_endpoint", minioConfig.getEndPoint());
        return JsonResponse.data(data);
    }

}
