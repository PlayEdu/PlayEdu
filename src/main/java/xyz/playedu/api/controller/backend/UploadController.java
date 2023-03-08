package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.request.backend.UploadVideoMergeRequest;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.UploadService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;


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
    private MinioService minioService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/file")
    public JsonResponse file(@RequestParam HashMap<String, Object> params, MultipartFile file) throws ServiceException {
        String categoryIds = MapUtils.getString(params, "category_ids");
        Resource res = uploadService.storeMinio(file, categoryIds);
        return JsonResponse.data(res);
    }

    @GetMapping("/minio/upload-id")
    public JsonResponse minioUploadId(@RequestParam HashMap<String, Object> params) {
        String extension = MapUtils.getString(params, "extension");
        if (extension == null || extension.trim().length() == 0) {
            return JsonResponse.error("extension参数为空");
        }
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(extension.toLowerCase());
        if (type == null) {
            return JsonResponse.error("该格式文件不支持上传");
        }

        String filename = HelperUtil.randomString(32) + "." + extension;//文件名
        String path = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + filename;//存储路径
        String uploadId = minioService.uploadId(path);

        HashMap<String, String> data = new HashMap<>();
        data.put("resource_type", type);
        data.put("upload_id", uploadId);
        data.put("filename", path);

        return JsonResponse.data(data);
    }

    @GetMapping("/minio/pre-sign-url")
    public JsonResponse minioPreSignUrl(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        Integer partNumber = MapUtils.getInteger(params, "part_number");
        String filename = MapUtils.getString(params, "filename");

        String url = minioService.chunkPreSignUrl(filename, partNumber + "", uploadId);

        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);

        return JsonResponse.data(data);
    }

    @PostMapping("/minio/merge-video")
    public JsonResponse minioMergeVideo(@RequestBody @Validated UploadVideoMergeRequest req) throws ServiceException {
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(req.getExtension());
        if (type == null) {
            return JsonResponse.error("当前格式不支持上传");
        }
        // 合并视频文件
        String url = minioService.merge(req.getFilename(), req.getUploadId());

        // 视频素材保存
        Resource videoResource = resourceService.create(
                req.getCategoryIds(),
                type,
                req.getOriginalFilename(),
                req.getExtension(),
                req.getSize(),
                BackendConstant.STORAGE_DRIVER_MINIO,
                "",
                req.getFilename(),
                url
        );
        // 视频封面素材保存
        Resource posterResource = uploadService.storeBase64Image(req.getPoster(), null);
        // 视频的封面素材改为[隐藏 && 属于视频的子素材]
        resourceService.changeParentId(posterResource.getId(), videoResource.getId());
        // 视频信息
        resourceService.storeResourceVideo(videoResource.getId(), req.getDuration(), posterResource.getUrl());

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", url);

        return JsonResponse.data(data);
    }

    @GetMapping("/minio/merge")
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
