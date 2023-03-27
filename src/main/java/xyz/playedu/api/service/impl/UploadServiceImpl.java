package xyz.playedu.api.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.constant.FrontendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.UserUploadImageLog;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.UploadService;
import xyz.playedu.api.service.UserUploadImageLogService;
import xyz.playedu.api.types.UploadFileInfo;
import xyz.playedu.api.util.Base64Util;
import xyz.playedu.api.util.HelperUtil;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/8 14:02
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private UserUploadImageLogService userUploadImageLogService;

    @Override
    @SneakyThrows
    public UploadFileInfo upload(MultipartFile file, String dir) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ServiceException("请上传文件");
        }

        // 上传上来的文件名名
        String filename = file.getOriginalFilename();

        UploadFileInfo fileInfo = new UploadFileInfo();
        // 文件大小
        fileInfo.setSize(file.getSize());
        // 解析扩展名
        fileInfo.setExtension(HelperUtil.fileExt(filename));
        // 解析扩展名称对应的系统资源类型
        fileInfo.setResourceType(BackendConstant.RESOURCE_EXT_2_TYPE.get(fileInfo.getExtension()));
        // 检测是否为系统不支持的资源类型
        if (fileInfo.getResourceType() == null) {
            throw new ServiceException("当前资源扩展不支持上传");
        }

        // 上传原文件的文件名
        fileInfo.setOriginalName(filename.replaceAll("." + fileInfo.getExtension(), ""));
        // 自定义新的存储文件名
        fileInfo.setSaveName(HelperUtil.randomString(32) + "." + fileInfo.getExtension());
        // 生成保存的相对路径
        if (dir == null || dir.length() == 0) {
            dir = BackendConstant.RESOURCE_TYPE_2_DIR.get(fileInfo.getResourceType());
        }
        fileInfo.setSavePath(dir + fileInfo.getSaveName());
        // 保存文件并生成访问url
        String url = minioService.saveFile(file, fileInfo.getSavePath(), BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(fileInfo.getExtension()));
        fileInfo.setUrl(url);

        return fileInfo;
    }

    @Override
    @SneakyThrows
    public Resource storeMinio(Integer adminId, MultipartFile file, String categoryIds) {
        UploadFileInfo info = upload(file, null);

        return resourceService.create(
                adminId,
                categoryIds,
                info.getResourceType(),
                info.getOriginalName(),
                info.getExtension(),
                file.getSize(),
                BackendConstant.STORAGE_DRIVER_MINIO,
                "",
                info.getSavePath(),
                info.getUrl()
        );
    }

    @Override
    @SneakyThrows
    public Resource storeBase64Image(Integer adminId, String content, String categoryIds) {
        // data:image/jpeg;base64,
        String[] base64Rows = content.split(",");
        // 解析出content-type
        String contentType = base64Rows[0].replaceAll("data:", "").replaceAll(";base64", "").toLowerCase();
        // 解析出文件格式
        String ext = contentType.replaceAll("image/", "");
        // 通过文件格式解析资源类型
        String type = BackendConstant.RESOURCE_EXT_2_TYPE.get(ext);
        // 资源类型必须存在
        if (type == null) {
            throw new ServiceException("资源类型不支持");
        }
        byte[] binary = Base64Util.decode(base64Rows[1]);

        String filename = HelperUtil.randomString(32) + "." + ext;
        String savePath = BackendConstant.RESOURCE_TYPE_2_DIR.get(type) + filename;

        // 保存文件
        String url = minioService.saveBytes(binary, savePath, BackendConstant.RESOURCE_EXT_2_CONTENT_TYPE.get(ext));
        // 上传记录
        return resourceService.create(adminId, categoryIds, type, filename, ext, (long) binary.length, BackendConstant.STORAGE_DRIVER_MINIO, "", savePath, url);
    }

    @Override
    @SneakyThrows
    public UserUploadImageLog userAvatar(Integer userId, MultipartFile file, String typed, String scene) {
        UploadFileInfo info = upload(file, FrontendConstant.DIR_AVATAR);
        UserUploadImageLog log = new UserUploadImageLog();
        log.setUserId(userId);
        log.setTyped(typed);
        log.setScene(scene);
        log.setSize(info.getSize());
        log.setDriver(BackendConstant.STORAGE_DRIVER_MINIO);
        log.setPath(info.getSavePath());
        log.setUrl(info.getUrl());
        log.setName(info.getOriginalName());
        log.setCreatedAt(new Date());
        userUploadImageLogService.save(log);
        return log;
    }
}
