package xyz.playedu.api.service.impl;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.config.MinioConfig;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.vendor.PlayEduMinioClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/7 13:29
 */
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioConfig c;

    @Autowired
    private MinioClient client;

    @Autowired
    private PlayEduMinioClient playEduMinioClient;

    @Override
    public String url(String path) {
        return c.getDomain() + c.getBucket() + "/" + path;
    }

    @Override
    @SneakyThrows
    public String saveFile(MultipartFile file, String savePath, String contentType) {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(c.getBucket())
                .object(savePath)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build();
        client.putObject(objectArgs);

        return url(savePath);
    }

    @Override
    public String uploadId(String path) {
        return playEduMinioClient.uploadId(c.getBucket(), path);
    }

    @Override
    @SneakyThrows
    public String chunkPreSignUrl(String filename, String partNumber, String uploadId) {
        Map<String, String> extraQueryParams = new HashMap<>();
        extraQueryParams.put("partNumber", partNumber + "");
        extraQueryParams.put("uploadId", uploadId);

        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(c.getBucket())
                        .object(filename)
                        .method(Method.PUT)
                        .expiry(60 * 60 * 24)
                        .extraQueryParams(extraQueryParams)
                        .build()
        );
    }

    @Override
    public String merge(String filename, String uploadId) {
        playEduMinioClient.merge(c.getBucket(), filename, uploadId);
        return url(filename);
    }

    @Override
    @SneakyThrows
    public void removeByPath(String path) {
        client.removeObject(RemoveObjectArgs.builder().bucket(c.getBucket()).object(path).build());
    }

    @Override
    @SneakyThrows
    public String saveBytes(byte[] file, String savePath, String contentType) {
        InputStream inputStream = new ByteArrayInputStream(file);

        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(c.getBucket())
                .object(savePath)
                .stream(inputStream, file.length, -1)
                .contentType(contentType)
                .build();

        client.putObject(objectArgs);

        return url(savePath);
    }
}
