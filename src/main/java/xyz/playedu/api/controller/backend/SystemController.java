/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.constant.CConfig;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.ImageCaptchaResult;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.RequestUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/v1/system")
@Slf4j
public class SystemController {

    @Autowired private ImageCaptchaService imageCaptchaService;

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
        Map<String, String> configData = BCtx.getConfig();

        String apiUrl = configData.get(CConfig.SYSTEM_API_URL);
        if (apiUrl == null || apiUrl.trim().length() == 0) {
            apiUrl = RequestUtil.uriWithProtocol();
        }

        HashMap<String, Object> data = new HashMap<>();

        data.put(CConfig.SYSTEM_NAME, configData.get(CConfig.SYSTEM_NAME));
        data.put(CConfig.SYSTEM_LOGO, configData.get(CConfig.SYSTEM_LOGO));
        data.put(CConfig.SYSTEM_API_URL, apiUrl);
        data.put(CConfig.SYSTEM_PC_URL, configData.get(CConfig.SYSTEM_PC_URL));
        data.put(CConfig.SYSTEM_H5_URL, configData.get(CConfig.SYSTEM_H5_URL));

        // 学员的默认头像
        String memberDefaultAvatar = configData.get(CConfig.MEMBER_DEFAULT_AVATAR);
        if (memberDefaultAvatar == null || memberDefaultAvatar.trim().length() == 0) {
            data.put(CConfig.MEMBER_DEFAULT_AVATAR, apiUrl + "/images/default_avatar.png");
        }

        // 内置的三个线上课封面
        List<String> defaultCourseThumbs = new ArrayList<>();
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb1.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb2.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb3.png");
        data.put("default_course_thumbs", defaultCourseThumbs);

        return JsonResponse.data(data);
    }
}
