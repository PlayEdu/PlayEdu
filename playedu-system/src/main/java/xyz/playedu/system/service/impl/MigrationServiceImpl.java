/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.system.domain.Migration;
import xyz.playedu.system.mapper.MigrationMapper;
import xyz.playedu.system.service.MigrationService;

import java.util.List;

/**
 * @author tengyongzhi
 * @description 针对表【migrations】的数据库操作Service实现
 * @createDate 2023-08-27 12:40:00
 */
@Service
public class MigrationServiceImpl extends ServiceImpl<MigrationMapper, Migration>
        implements MigrationService {
    @Override
    public List<String> all() {
        return list().stream().map(Migration::getMigration).toList();
    }

    @Override
    public void store(String name) {
        Migration migration = new Migration();
        migration.setMigration(name);
        save(migration);
    }
}
