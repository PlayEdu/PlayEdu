package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

import java.util.Arrays;
import java.util.HashMap;

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
    private MinioService minioService;

    @PostMapping("/image")
    public JsonResponse image(@RequestParam HashMap<String, Object> params, MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return JsonResponse.error("请上传文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(BackendConstant.UPLOAD_IMAGE_CONTENT_TYPE_WL).contains(contentType)) {
            return JsonResponse.error("格式不支持");
        }

        Integer cid = MapUtils.getInteger(params, "category_id");
        if (cid != null && !cid.equals(0) && resourceCategoryService.getById(cid) == null) {
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

        // 保存文件
        String url = minioService.saveFile(file, savePath, contentType);
        // 上传记录
        Resource res = resourceService.create(cid, BackendConstant.RESOURCE_TYPE_IMAGE, oldFilename, ext, file.getSize(), "minio", "", savePath, url);

        return JsonResponse.data(res);
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

        String filename = HelperUtil.randomString(32) + "." + extension;//文件名
        String path = BackendConstant.UPLOAD_VIDEO_DIR + filename;//存储路径
        String uploadId = minioService.uploadId(path);

        HashMap<String, String> data = new HashMap<>();
        data.put("resource_type", BackendConstant.RESOURCE_EXT_2_TYPE.get(extension.toLowerCase()));
        data.put("upload_id", uploadId);
        data.put("filename", path);

        return JsonResponse.data(data);
    }

    @GetMapping("/minio-pre-sign-url")
    public JsonResponse minioPreSignUrl(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        Integer partNumber = MapUtils.getInteger(params, "part_number");
        String filename = MapUtils.getString(params, "filename");

        String url = minioService.chunkPreSignUrl(filename, partNumber + "", uploadId);

        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);

        return JsonResponse.data(data);
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

        String url = minioService.merge(filename, uploadId);

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", url);

        return JsonResponse.data(data);
    }

}
