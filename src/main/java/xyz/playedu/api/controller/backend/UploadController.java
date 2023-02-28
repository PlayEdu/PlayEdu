package xyz.playedu.api.controller.backend;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.config.MinioConfig;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/28 16:26
 */
@RestController
@RequestMapping("/backend/v1/upload")
public class UploadController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient minioClient;

    @PostMapping("/image")
    public JsonResponse image(@RequestParam HashMap<String, Object> params, MultipartFile file) {
        Integer categoryId = MapUtils.getInteger(params, "category_id", 0);
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return JsonResponse.error("请上传文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(BackendConstant.UPLOAD_IMAGE_CONTENT_TYPE_WL).contains(contentType)) {
            return JsonResponse.error("格式不支持");
        }

        String filename = file.getOriginalFilename();
        String ext = HelperUtil.fileExt(filename);
        if (!Arrays.asList(BackendConstant.UPLOAD_IMAGE_EXT_WL).contains(ext)) {
            return JsonResponse.error("格式不支持");
        }

        String oldFilename = filename.replaceAll("." + ext, "");
        String newFilename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.UPLOAD_IMAGE_DIR + newFilename;

        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(savePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build();

            minioClient.putObject(objectArgs);

            String url = minioConfig.getDomain() + minioConfig.getBucket() + "/" + savePath;

            Resource resource = new Resource();
            resource.setCategoryId(categoryId);
            resource.setName(oldFilename);
            resource.setExtension(ext);
            resource.setSize(file.getSize());
            resource.setDisk("minio");
            resource.setFileId("");
            resource.setPath(savePath);
            resource.setUrl(url);
            resource.setCreatedAt(new Date());
            resourceService.save(resource);

            return JsonResponse.data(resource);
        } catch (Exception e) {
            return JsonResponse.error("系统错误");
        }
    }

}
