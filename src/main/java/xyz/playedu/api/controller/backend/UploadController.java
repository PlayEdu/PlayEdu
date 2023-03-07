package xyz.playedu.api.controller.backend;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.config.MinioConfig;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.vendor.PlayEduMinioClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/28 16:26
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/upload")
public class UploadController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private PlayEduMinioClient playEduMinioClient;

    @PostMapping("/image")
    public JsonResponse image(@RequestParam HashMap<String, Object> params, MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return JsonResponse.error("请上传文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(BackendConstant.UPLOAD_IMAGE_CONTENT_TYPE_WL).contains(contentType)) {
            return JsonResponse.error("格式不支持");
        }

        Integer categoryId = MapUtils.getInteger(params, "category_id");
        if (categoryId != null && !categoryId.equals(0) && resourceCategoryService.getById(categoryId) == null) {
            return JsonResponse.error("分类不存在");
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
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(minioConfig.getBucket()).object(savePath).stream(file.getInputStream(), file.getSize(), -1).contentType(contentType).build();

            minioClient.putObject(objectArgs);

            String url = minioConfig.getDomain() + minioConfig.getBucket() + "/" + savePath;

            Resource res = resourceService.create(categoryId, BackendConstant.RESOURCE_TYPE_IMAGE, oldFilename, ext, file.getSize(), "minio", "", savePath, url);
            return JsonResponse.data(res);
        } catch (Exception e) {
            return JsonResponse.error("系统错误");
        }
    }

    @GetMapping("/minio-upload-id")
    public JsonResponse minioUploadId(@RequestParam HashMap<String, Object> params) {
        String extension = MapUtils.getString(params, "extension");
        if (extension == null || extension.trim().length() == 0) {
            return JsonResponse.error("extension参数为空");
        }
        String contentType = BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(extension.toLowerCase());
        if (contentType == null) {
            return JsonResponse.error("该格式不支持上传");
        }
        String resourceType = BackendConstant.RESOURCE_EXT_2_TYPE.get(extension.toLowerCase());

        try {
            String filename = HelperUtil.randomString(32) + "." + extension;
            String path = BackendConstant.UPLOAD_VIDEO_DIR + filename;
            String uploadId = playEduMinioClient.uploadId(minioConfig.getBucket(), path);

            HashMap<String, String> data = new HashMap<>();
            data.put("resource_type", resourceType);
            data.put("upload_id", uploadId);
            data.put("filename", path);

            return JsonResponse.data(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResponse.error("系统错误");
        }
    }

    @GetMapping("/minio-pre-sign-url")
    public JsonResponse minioPreSignUrl(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        Integer partNumber = MapUtils.getInteger(params, "part_number");
        String filename = MapUtils.getString(params, "filename");

        try {
            Map<String, String> extraQueryParams = new HashMap<>();
            extraQueryParams.put("partNumber", partNumber + "");
            extraQueryParams.put("uploadId", uploadId);

            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(minioConfig.getBucket()).object(filename).method(Method.PUT).expiry(60 * 60 * 24).extraQueryParams(extraQueryParams).build());

            HashMap<String, String> data = new HashMap<>();
            data.put("url", url);

            return JsonResponse.data(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResponse.error("系统错误");
        }
    }

    @GetMapping("/minio-merge")
    public JsonResponse minioMerge(@RequestParam HashMap<String, Object> params) {
        String filename = MapUtils.getString(params, "filename");
        String uploadId = MapUtils.getString(params, "upload_id");
        if (filename == null || filename.trim().length() == 0) {
            return JsonResponse.error("filename必填");
        }
        if (uploadId == null || uploadId.trim().length() == 0) {
            return JsonResponse.error("uploadId必填");
        }
        playEduMinioClient.merge(minioConfig.getBucket(), filename, uploadId);

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", minioConfig.getDomain() + minioConfig.getBucket() + "/" + filename);

        return JsonResponse.data(data);
    }

}
