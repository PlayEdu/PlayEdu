package xyz.playedu.api.service;

import xyz.playedu.api.domain.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Service
 * @createDate 2023-02-23 10:50:26
 */
public interface ResourceService extends IService<Resource> {

    PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter);

    Resource create(Integer adminId, String categoryIds, String type, String filename, String ext, Long size, String disk, String fileId, String path, String url);

    Resource findOrFail(Integer id) throws NotFoundException;

    void changeParentId(Integer id, Integer parentId);

    void storeResourceVideo(Integer rid, Integer duration, String poster);

    List<Resource> chunks(List<Integer> ids);

}
