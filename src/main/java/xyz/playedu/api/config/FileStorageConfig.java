package xyz.playedu.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/28 16:38
 */
@Data
@Configuration
public class FileStorageConfig {

    @Value("${spring.file-storage.default-platform}")
    private String defaultPlatform;

    @Value("${spring.file-storage.minio[0].domain}")
    private String domain;

    @Value("${spring.file-storage.minio[0].bucket-name}")
    private String bucket;

    @Value("${spring.file-storage.minio[0].base-path}")
    private String basePath;

}
