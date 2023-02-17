package xyz.playedu.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

public class RequestUtil {

    /**
     * 获取请求对象
     *
     * @return HttpServletRequest
     * @author fzr
     */
    public static HttpServletRequest handler() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public static String token() {
        HttpServletRequest request = RequestUtil.handler();
        if (request == null) {
            return "";
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.length() == 0 || token.split(" ").length != 2) {
            return "";
        }
        return token.split(" ")[1];
    }

    /**
     * 获取不带参请求URl
     * 示例: https://127.0.0.1:8082/api/system/menu/menus
     *
     * @return String
     * @author fzr
     */
    public static String url() {
        HttpServletRequest request = RequestUtil.handler();
        if (request != null) {
            return request.getRequestURL().toString();
        }
        return "";
    }

    /**
     * 获取带端口的请求地址
     * 示例: https://127.0.0.1:8082
     *
     * @return String
     * @author fzr
     */
    public static String uri() {
        String domain = RequestUtil.domain();
        if (!Arrays.asList(443, 80, 0).contains(RequestUtil.port())) {
            domain += ":" + RequestUtil.port();
        }

        return domain;
    }

    /**
     * 获取请求路由
     * 示例: /api/system/menu/menus
     *
     * @return String
     * @author fzr
     */
    public static String route() {
        HttpServletRequest request = RequestUtil.handler();
        if (request != null) {
            return request.getRequestURI();
        }
        return "";
    }

    /**
     * 获取请求端口
     * 示例: 443/80
     *
     * @return Integer
     * @author fzr
     */
    public static Integer port() {
        HttpServletRequest request = RequestUtil.handler();
        if (request != null) {
            return request.getServerPort();
        }
        return 0;
    }

    public static String domain() {
        HttpServletRequest request = RequestUtil.handler();
        if (request != null) {
            String requestUrl = request.getRequestURL().toString();
            List<String> urls = Arrays.asList(requestUrl.split("/"));

            return urls.get(2).split(":")[0];
        }
        return null;
    }

    public static Boolean isGet() {
        return isMethod("GET");
    }

    public static Boolean isPost() {
        return isMethod("POST");
    }

    public static Boolean isPUT() {
        return isMethod("PUT");
    }

    public static Boolean isDelete() {
        return isMethod("DELETE");
    }

    public static boolean isMethod(String method) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals(method);
        }
        return false;
    }

}
