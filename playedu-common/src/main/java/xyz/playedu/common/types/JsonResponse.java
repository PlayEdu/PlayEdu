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
package xyz.playedu.common.types;

import lombok.Data;

@Data
public class JsonResponse {

    private Integer code;
    private String msg;
    private Object data;

    public JsonResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResponse success(String msg) {
        return new JsonResponse(0, msg, null);
    }

    public static JsonResponse success() {
        return new JsonResponse(0, "", null);
    }

    public static JsonResponse data(Object data) {
        return new JsonResponse(0, "", data);
    }

    public static JsonResponse error(String msg, Integer code) {
        return new JsonResponse(code, msg, null);
    }

    public static JsonResponse error(String msg) {
        return new JsonResponse(-1, msg, null);
    }

    public static JsonResponse error(String msg, Object data) {
        return new JsonResponse(-1, msg, data);
    }
}
