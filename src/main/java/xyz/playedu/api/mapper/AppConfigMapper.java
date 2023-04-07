/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.AppConfig;

/**
 * @author tengteng
 * @description 针对表【app_config】的数据库操作Mapper
 * @createDate 2023-03-09 13:55:39 @Entity xyz.playedu.api.domain.AppConfig
 */
@Mapper
public interface AppConfigMapper extends BaseMapper<AppConfig> {}
