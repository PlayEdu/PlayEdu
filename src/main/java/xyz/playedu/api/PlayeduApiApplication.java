/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import xyz.playedu.api.config.UniqueNameGenerator;

@SpringBootApplication
@EnableAsync
@ComponentScan(nameGenerator = UniqueNameGenerator.class)
public class PlayeduApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayeduApiApplication.class, args);
    }
}
