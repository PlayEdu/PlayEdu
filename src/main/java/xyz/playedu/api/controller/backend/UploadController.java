package xyz.playedu.api.controller.backend;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.config.FileStorageConfig;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

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
    private FileStorageService fileStorageService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private FileStorageConfig fileStorageConfig;

    @PostMapping("/image")
    public JsonResponse image(@RequestParam HashMap<String, Object> params, MultipartFile file) {
        Integer categoryId = MapUtils.getInteger(params, "category_id", 0);
        if (file == null || file.isEmpty()) {
            return JsonResponse.error("请上传文件");
        }
        FileInfo fileInfo = fileStorageService
                .of(file)
                .setPath(fileStorageConfig.getBasePath() + BackendConstant.UPLOAD_IMAGE_DIR)
                .setObjectId(HelperUtil.randomString(32))
                .upload();

        String savePath = fileInfo.getPath() + fileInfo.getFilename();
        String url = fileStorageConfig.getDomain() + fileStorageConfig.getBucket() + "/" + savePath;

        Resource resource = new Resource();
        resource.setCategoryId(categoryId);
        resource.setName(fileInfo.getFilename());
        resource.setExtension(fileInfo.getExt());
        resource.setSize(fileInfo.getSize());
        resource.setDisk(fileStorageConfig.getDefaultPlatform());
        resource.setFileId(fileInfo.getObjectId());
        resource.setPath(savePath);
        resource.setUrl(url);
        resource.setCreatedAt(new Date());
        resourceService.save(resource);

        return JsonResponse.data(resource);
    }

}
