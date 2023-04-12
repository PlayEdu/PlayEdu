/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.Department;
import xyz.playedu.api.types.mapper.DepartmentsUserCountMapRes;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Mapper
 * @createDate 2023-02-19 12:19:45 @Entity xyz.playedu.api.domain.Department
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<DepartmentsUserCountMapRes> getDepartmentsUserCount();
}
