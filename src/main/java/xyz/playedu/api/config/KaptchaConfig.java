package xyz.playedu.api.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

@Configuration
public class KaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否边框
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 字符颜色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
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
