package xyz.playedu.api.service;

import xyz.playedu.api.domain.ResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service
 * @createDate 2023-02-23 09:50:18
 */
public interface ResourceCategoryService extends IService<ResourceCategory> {

    List<ResourceCategory> getByType(String type);

}
