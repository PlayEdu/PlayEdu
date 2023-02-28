package xyz.playedu.api;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableFileStorage
public class PlayeduApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayeduApiApplication.class, args);
    }

}
