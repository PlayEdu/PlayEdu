/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import org.springframework.web.multipart.MultipartFile;

import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.UserUploadImageLog;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.UploadFileInfo;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/8 14:02
 */
public interface UploadService {

    UploadFileInfo upload(MultipartFile file, String dir) throws ServiceException;

    Resource storeMinio(Integer adminId, MultipartFile file, String categoryIds)
            throws ServiceException;

    Resource storeBase64Image(Integer adminId, String content, String categoryIds)
            throws ServiceException;

    UserUploadImageLog userAvatar(Integer userId, MultipartFile file, String typed, String scene);
}
