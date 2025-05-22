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
package xyz.playedu.api.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.MemoryCacheUtil;

@RestController
@Slf4j
@RequestMapping("/backend/v1/cache")
public class CacheController {

    @Autowired private MemoryCacheUtil memoryCacheUtil;

    @GetMapping("/list")
    @Log(title = "缓存列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse list() {
        Map<String, Object> data = new HashMap<>();
        data.put("keys", memoryCacheUtil.getAllKeys());
        data.put("cache", memoryCacheUtil.getAllCache());
        return JsonResponse.data(data);
    }

    @DeleteMapping("/clear")
    @Log(title = "缓存删除key", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse clear(@RequestParam HashMap<String, Object> params) {
        String cache_key = MapUtils.getString(params, "cache_key");
        memoryCacheUtil.del(cache_key);
        return JsonResponse.success();
    }

    @DeleteMapping("/clear/all")
    @Log(title = "缓存清空", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse clearAll() {
        List<String> keys = memoryCacheUtil.getAllKeys();
        for (String key : keys) {
            MemoryCacheUtil.del(key);
        }
        return JsonResponse.success();
    }
}
