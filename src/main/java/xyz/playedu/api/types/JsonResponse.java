/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.types;

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
