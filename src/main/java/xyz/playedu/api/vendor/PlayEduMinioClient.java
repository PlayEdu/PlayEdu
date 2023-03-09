package xyz.playedu.api.vendor;

import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/6 16:12
 */
public class PlayEduMinioClient extends MinioAsyncClient {
    public PlayEduMinioClient(MinioAsyncClient client) {
        super(client);
    }

    @SneakyThrows
    public String uploadId(String bucket, String filename) {
        CreateMultipartUploadResponse response = super.createMultipartUpload(bucket, null, filename, null, null);
        return response.result().uploadId();
    }

    @SneakyThrows
    public void merge(String bucketName, String objectName, String uploadId) {
        ListPartsResponse listPartsResponse = super.listParts(bucketName, null, objectName, 10000, 0, uploadId, null, null);
        List<Part> partList = listPartsResponse.result().partList();
        Part[] parts = new Part[10000];
        int partNumber = 1;
        for (Part part : partList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }
        super.completeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
    }
}
