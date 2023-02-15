package xyz.playedu.api.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.ImageCaptchaResult;
import xyz.playedu.api.util.Base64Util;
import xyz.playedu.api.util.RedisUtil;
import xyz.playedu.api.util.ToolUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    @Value("${playedu.captcha.cache-prefix}")
    private String ConfigCachePrefix;

    @Value("${playedu.captcha.expire}")
    private Long ConfigExpire;

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Override
    public ImageCaptchaResult generate() throws IOException {
        ImageCaptchaResult imageCaptcha = new ImageCaptchaResult();
        BufferedImage image;

        // 图形验证码的key[api是无状态的需要key来锁定验证码的值]
        String randomKey = ToolUtil.randomString(16);
        imageCaptcha.setKey(randomKey);

        // 生成验证码
        imageCaptcha.setCode(defaultKaptcha.createText());
        image = defaultKaptcha.createImage(imageCaptcha.getCode());

        // 写入到redis中
        RedisUtil.set(getCacheKey(randomKey), imageCaptcha.getCode(), ConfigExpire);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "png", os);

        String base64 = "data:image/png;base64," + Base64Util.encode(os.toByteArray());
        imageCaptcha.setImage(base64);

        return imageCaptcha;
    }

    @Override
    public boolean verify(String key, String code) {
        String cacheKey = getCacheKey(key);
        Object queryResult = RedisUtil.get(cacheKey);
        if (queryResult == null) {//未查找到[已过期 | 不存在]
            return false;
        }
        String cacheValue = (String) queryResult;
        boolean verifyResult = cacheValue.equals(code);

        if (verifyResult) {//验证成功删除缓存->防止多次使用
            RedisUtil.del(cacheKey);
        }

        return verifyResult;
    }

    private String getCacheKey(String val) {
        return ConfigCachePrefix + val;
    }
}
