package xyz.playedu.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class PlayEduConfig {

    @Value("${spring.profiles.active}")
    private String env;

}
