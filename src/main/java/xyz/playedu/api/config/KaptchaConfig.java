/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.config;

import static com.google.code.kaptcha.Constants.*;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否边框
        properties.setProperty(KAPTCHA_BORDER, "no");
        // 字符颜色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "red");
        // 干扰线颜色
        properties.setProperty(KAPTCHA_NOISE_COLOR, "red");
        // 字符间距
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
        // 图片宽度
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "120");
        // 图片高度
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "50");
        // 字符大小
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "40");
        // 字符长度
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // 字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        defaultKaptcha.setConfig(new Config(properties));

        return defaultKaptcha;
    }
}
