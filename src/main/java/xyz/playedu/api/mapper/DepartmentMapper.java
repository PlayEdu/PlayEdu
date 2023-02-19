package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【departments】的数据库操作Mapper
* @createDate 2023-02-19 10:39:57
* @Entity xyz.playedu.api.domain.Department
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}




