package xyz.playedu.api.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/7 13:29
 */
public interface MinioService {

    String url(String path);

    String saveFile(MultipartFile file, String savePath, String contentType);

    String saveBytes(byte[] file, String savePath, String contentType);

    String uploadId(String path);

    String chunkPreSignUrl(String filename, String partNumber, String uploadId);

    String merge(String filename, String uploadId);

    void removeByPath(String path);

}
