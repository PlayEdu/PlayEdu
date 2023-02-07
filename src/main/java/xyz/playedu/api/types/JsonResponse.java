package xyz.playedu.api.types;

public class JsonResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResponse<String> success(String msg) {
        return new JsonResponse<>(0, msg, null);
    }

    public static JsonResponse<Object> data(Object data) {
        return new JsonResponse<>(0, "", data);
    }

    public static JsonResponse<String> error(String msg, Integer code) {
        return new JsonResponse<>(code, msg, null);
    }
}
