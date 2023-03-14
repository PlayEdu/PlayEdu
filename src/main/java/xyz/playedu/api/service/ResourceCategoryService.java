package xyz.playedu.api.service;

import xyz.playedu.api.domain.ResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service
 * @createDate 2023-02-23 09:50:18
 */
public interface ResourceCategoryService extends IService<ResourceCategory> {

    List<ResourceCategory> listByParentId(Integer id);

    List<ResourceCategory> all();

    ResourceCategory findOrFail(Integer id) throws NotFoundException;

    void deleteById(Integer id) throws NotFoundException;

    void update(ResourceCategory category, String name, Integer parentId, Integer sort) throws NotFoundException;

    void create(String name, Integer parentId, Integer sort) throws NotFoundException;

    String childrenParentChain(ResourceCategory category);

    String compParentChain(Integer parentId) throws NotFoundException;

    List<Integer> getCourseIdsById(Integer id);

    List<Integer> getRidsById(Integer id);

    void resetSort(List<Integer> ids);

    void changeParent(Integer id, Integer parentId, List<Integer> ids) throws NotFoundException;

    Map<Integer, List<ResourceCategory>> groupByParent();

}
