/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.controller.backend;

import com.amazonaws.services.s3.model.PartSummary;
import java.util.HashMap;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.request.backend.UploadFileMergeRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.constant.CommonConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.UploadFileInfo;
import xyz.playedu.common.types.config.S3Config;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.S3Util;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.service.ResourceExtraService;
import xyz.playedu.resource.service.ResourceService;
import xyz.playedu.resource.service.UploadService;

@RestController
@Slf4j
@RequestMapping("/backend/v1/upload")
public class UploadController {

    @Autowired private UploadService uploadService;

    @Autowired private AppConfigService appConfigService;

    @Autowired private ResourceService resourceService;

    @Autowired private ResourceExtraService resourceExtraService;

    @BackendPermission(slug = BPermissionConstant.UPLOAD)
    @PostMapping("/minio")
    @Log(title = "上传-MinIO", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse uploadMinio(
            @RequestParam HashMap<String, Object> params, MultipartFile file)
            throws ServiceException {
        // 校验存储配置是否完整
        S3Config s3Config = appConfigService.getS3Config();
        if (StringUtil.isEmpty(s3Config.getAccessKey())
                || StringUtil.isEmpty(s3Config.getSecretKey())
                || StringUtil.isEmpty(s3Config.getBucket())
                || StringUtil.isEmpty(s3Config.getEndpoint())
                || StringUtil.isEmpty(s3Config.getRegion())) {
            throw new ServiceException("存储服务未配置");
        }
        String categoryIds = MapUtils.getString(params, "category_ids");

        UploadFileInfo info = uploadService.upload(s3Config, file, null);

        Resource res =
                resourceService.create(
                        BCtx.getId(),
                        categoryIds,
                        info.getResourceType(),
                        info.getOriginalName(),
                        info.getExtension(),
                        file.getSize(),
                        "",
                        info.getSavePath(),
                        CommonConstant.ZERO,
                        CommonConstant.ZERO);
        return JsonResponse.data(res);
    }

    @BackendPermission(slug = BPermissionConstant.UPLOAD)
    @GetMapping("/minio/upload-id")
    @Log(title = "上传-MinIO-uploadId", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse minioUploadId(@RequestParam HashMap<String, Object> params) {
        // 校验存储配置是否完整
        S3Config s3Config = appConfigService.getS3Config();
        if (StringUtil.isEmpty(s3Config.getAccessKey())
                || StringUtil.isEmpty(s3Config.getSecretKey())
                || StringUtil.isEmpty(s3Config.getBucket())
                || StringUtil.isEmpty(s3Config.getEndpoint())
                || StringUtil.isEmpty(s3Config.getRegion())) {
            throw new ServiceException("存储服务未配置");
        }

        String extension = MapUtils.getString(params, "extension");
        if (extension == null || extension.trim().isEmpty()) {
            return JsonResponse.error("extension参数为空");
        }
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(extension.toLowerCase());
        if (type == null) {
            return JsonResponse.error("该格式文件不支持上传");
        }

        S3Util s3Util = new S3Util(s3Config);

        String filename = HelperUtil.randomString(32) + "." + extension; // 文件名
        String path = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + filename; // 存储路径
        String uploadId = s3Util.uploadId(path);

        HashMap<String, String> data = new HashMap<>();
        data.put("resource_type", type);
        data.put("upload_id", uploadId);
        data.put("filename", path);

        return JsonResponse.data(data);
    }

    @GetMapping("/minio/pre-sign-url")
    @Log(title = "上传-MinIO-签名URL", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse minioPreSignUrl(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        Integer partNumber = MapUtils.getInteger(params, "part_number");
        String filename = MapUtils.getString(params, "filename");

        String url =
                new S3Util(appConfigService.getS3Config())
                        .generatePartUploadPreSignUrl(filename, partNumber + "", uploadId);

        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);

        return JsonResponse.data(data);
    }

    @GetMapping("/minio/list-parts")
    @Log(title = "上传-MinIO-已上传查询", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse minioListParts(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        String filename = MapUtils.getString(params, "filename");

        List<PartSummary> parts =
                new S3Util(appConfigService.getS3Config()).listParts(uploadId, filename);

        return JsonResponse.data(parts);
    }

    @GetMapping("/minio/purge-segments")
    @Log(title = "上传-MinIO-已上传查询", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse purgeIncompleteSegments(@RequestParam HashMap<String, Object> params) {
        String uploadId = MapUtils.getString(params, "upload_id");
        String filename = MapUtils.getString(params, "filename");

        new S3Util(appConfigService.getS3Config()).purgeSegments(uploadId, filename);
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.UPLOAD)
    @PostMapping("/minio/merge-file")
    @Log(title = "上传-MinIO-文件合并", businessType = BusinessTypeConstant.UPLOAD)
    public JsonResponse minioMergeFile(@RequestBody @Validated UploadFileMergeRequest req)
            throws ServiceException {
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(req.getExtension());
        if (type == null) {
            return JsonResponse.error("当前格式不支持上传");
        }
        String extension = req.getExtension();
        String originalFilename = req.getOriginalFilename().replaceAll("(?i)." + extension, "");

        // 合并资源文件
        S3Config s3Config = appConfigService.getS3Config();
        S3Util s3Util = new S3Util(s3Config);
        s3Util.merge(req.getFilename(), req.getUploadId());

        // 资源素材保存
        Resource resource =
                resourceService.create(
                        BCtx.getId(),
                        req.getCategoryIds(),
                        type,
                        originalFilename,
                        extension,
                        req.getSize(),
                        "",
                        req.getFilename(),
                        CommonConstant.ZERO,
                        CommonConstant.ZERO);

        // 记录资源详情信息
        doSaveResourceExtra(resource, req.getPoster(), req.getDuration());

        return JsonResponse.success();
    }

    /** 记录资源详情信息 */
    @SneakyThrows
    public void doSaveResourceExtra(Resource resource, String poster, Integer duration) {
        log.info("资源文件,类型={},id={}", resource.getType(), resource.getId());
        String type = resource.getType();
        // 视频资源特殊处理--视频封面资源
        if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(type)) {
            Integer posterId = 0;
            if (StringUtil.isNotEmpty(poster)) {
                Resource posterResource =
                        uploadService.storeBase64Image(
                                appConfigService.getS3Config(), BCtx.getId(), poster, null);
                posterId = posterResource.getId();
            }
            resourceExtraService.create(resource.getId(), duration, posterId);
        }
    }
}
