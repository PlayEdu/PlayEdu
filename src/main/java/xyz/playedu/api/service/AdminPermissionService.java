package xyz.playedu.api.service;

import xyz.playedu.api.domain.AdminPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_permissions】的数据库操作Service
 * @createDate 2023-02-20 14:27:50
 */
public interface AdminPermissionService extends IService<AdminPermission> {

    HashMap<String, Boolean> allSlugs();

    List<AdminPermission> listOrderBySortAsc();

    HashMap<String,Boolean> getSlugsByIds(List<Integer> ids);

    List<Integer> allIds();

}
