package xyz.playedu.api.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.exception.ServiceException;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/8 14:02
 */
public interface UploadService {
    Resource storeMinio(MultipartFile file, Integer categoryId) throws ServiceException;

    Resource storeBase64Image(String content,Integer categoryId) throws ServiceException;
}
