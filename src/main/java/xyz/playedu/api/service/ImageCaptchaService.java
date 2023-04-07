/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import xyz.playedu.api.types.ImageCaptchaResult;

import java.io.IOException;

public interface ImageCaptchaService {

    ImageCaptchaResult generate() throws IOException;

    boolean verify(String key, String code);
}
