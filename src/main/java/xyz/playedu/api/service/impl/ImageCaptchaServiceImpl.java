/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import xyz.playedu.api.service.ImageCaptchaService;
import xyz.playedu.api.types.ImageCaptchaResult;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.RedisUtil;

@Slf4j
@Service
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    @Value("${playedu.captcha.cache-prefix}")
    private String ConfigCachePrefix;

    @Value("${playedu.captcha.expire}")
    private Long ConfigExpire;

    @Override
    public ImageCaptchaResult generate() {
        ImageCaptchaResult imageCaptcha = new ImageCaptchaResult();

        // 生成验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(240, 100, 4, 1);

        // 图形验证码的key[api是无状态的需要key来锁定验证码的值]
        String randomKey = HelperUtil.randomString(16);
        imageCaptcha.setKey(randomKey);
        imageCaptcha.setCode(lineCaptcha.getCode());
        imageCaptcha.setImage("data:image/png;base64," + lineCaptcha.getImageBase64());

        // 写入到redis中
        RedisUtil.set(getCacheKey(imageCaptcha.getKey()), imageCaptcha.getCode(), ConfigExpire);

        return imageCaptcha;
    }

    @Override
    public boolean verify(String key, String code) {
        String cacheKey = getCacheKey(key);
        Object queryResult = RedisUtil.get(cacheKey);
        if (queryResult == null) { // 未查找到[已过期 | 不存在]
            return false;
        }
        String cacheValue = (String) queryResult;
        boolean verifyResult = cacheValue.equalsIgnoreCase(code);

        if (verifyResult) { // 验证成功删除缓存->防止多次使用
            RedisUtil.del(cacheKey);
        }

        return verifyResult;
    }

    private String getCacheKey(String val) {
        return ConfigCachePrefix + val;
    }
}
