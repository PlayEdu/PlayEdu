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

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/8 14:02
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MinioService minioService;

    @Override
    public Resource storeMinio(MultipartFile file, Integer cid) throws ServiceException {
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

        // 分类校验
        if (cid != null && !cid.equals(0) && resourceCategoryService.find(cid, type) == null) {
            throw new ServiceException("分类不存在");
        }

        // 上传原文件的文件名
        String oFilename = filename.replaceAll("." + ext, "");
        // 自定义新的存储文件名
        String newFilename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + newFilename;

        // 保存文件
        String url = minioService.saveFile(file, savePath, contentType);
        // 上传记录
        return resourceService.create(cid, type, oFilename, ext, file.getSize(), BackendConstant.STORAGE_DRIVER_MINIO, "", savePath, url);
    }

    @Override
    public Resource storeBase64Image(String content, Integer categoryId) throws ServiceException {
        // data:image/jpeg;base64,
        String[] base64Rows = content.split(",");
        String contentType = base64Rows[0].replaceAll("data:", "").replaceAll(";base64", "").toLowerCase();
        String ext = contentType.replaceAll("image/", "");
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(ext);
        String safeContentType = BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(ext);
        if (type == null || safeContentType == null || !safeContentType.equals(contentType)) {
            throw new ServiceException("格式不支持");
        }
        byte[] binary = Base64Util.decode(base64Rows[1]);

        String filename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + filename;

        // 保存文件
        String url = minioService.saveBytes(binary, savePath, contentType);
        // 上传记录
        return resourceService.create(categoryId, type, filename, ext, (long) binary.length, BackendConstant.STORAGE_DRIVER_MINIO, "", savePath, url);
    }
}
