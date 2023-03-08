package xyz.playedu.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.UploadService;
import xyz.playedu.api.util.Base64Util;
import xyz.playedu.api.util.HelperUtil;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/8 14:02
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MinioService minioService;

    @Override
    public Resource storeMinio(MultipartFile file, String categoryIds) throws ServiceException {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ServiceException("请上传文件");
        }

        // 文件后缀名校验
        String filename = file.getOriginalFilename();
        String ext = HelperUtil.fileExt(filename);
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(ext);
        if (type == null) {
            throw new ServiceException("格式不支持");
        }

        // content-type校验
        String contentType = file.getContentType();
        String safeContentType = BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(ext);
        if (safeContentType == null || !safeContentType.equals(contentType)) {
            throw new ServiceException("格式不支持");
        }

        // 上传原文件的文件名
        String oFilename = filename.replaceAll("." + ext, "");
        // 自定义新的存储文件名
        String newFilename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + newFilename;

        // 保存文件
        String url = minioService.saveFile(file, savePath, contentType);
        // 上传记录
        return resourceService.create(categoryIds, type, oFilename, ext, file.getSize(), BackendConstant.STORAGE_DRIVER_MINIO, "", savePath, url);
    }

    @Override
    public Resource storeBase64Image(String content, String categoryIds) throws ServiceException {
        // data:image/jpeg;base64,
        String[] base64Rows = content.split(",");
        // 解析出content-type
        String contentType = base64Rows[0].replaceAll("data:", "").replaceAll(";base64", "").toLowerCase();
        // 解析出文件格式
        String ext = contentType.replaceAll("image/", "");
        // 通过文件格式解析资源类型
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(ext);
        // 通过资源类型获取安全的content-type
        String safeContentType = BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(ext);
        // 资源类型必须存在 && 安全的 content-type 必须存在 且与解析出来的 content-type 一致
        if (type == null || safeContentType == null || !safeContentType.equals(contentType)) {
            throw new ServiceException("格式不支持");
        }
        byte[] binary = Base64Util.decode(base64Rows[1]);

        String filename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + filename;

        // 保存文件
        String url = minioService.saveBytes(binary, savePath, contentType);
        // 上传记录
        return resourceService.create(categoryIds, type, filename, ext, (long) binary.length, BackendConstant.STORAGE_DRIVER_MINIO, "", savePath, url);
    }
}
