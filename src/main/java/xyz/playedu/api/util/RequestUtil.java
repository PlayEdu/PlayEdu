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

    /**
     * 获取请求域名
     * 示例: https://127.0.0.1
     *
     * @return String
     * @author fzr
     */
    public static String domain() {
        HttpServletRequest request = RequestUtil.handler();
        if (request != null) {
            String requestUrl = request.getRequestURL().toString();
            List<String> urls = Arrays.asList(requestUrl.split("/"));

            String agree = "http:";
            if (request.getServerPort() == 443) {
                agree = "https:";
            }

            return agree + "//" + urls.get(2).split(":")[0];
        }
        return null;
    }

    /**
     * 判断是否是GET请求
     *
     * @return Boolean
     * @author fzr
     */
    public static Boolean isGet() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("GET");
        }
        return false;
    }

    /**
     * 判断是否是POST请求
     *
     * @return Boolean
     * @author fzr
     */
    public static Boolean isPost() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("POST");
        }
        return false;
    }

    /**
     * 判断是否是PUT请求
     *
     * @return Boolean
     * @author fzr
     */
    public static Boolean isPUT() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("PUT");
        }
        return false;
    }

    /**
     * 判断是否是DELETE请求
     *
     * @return Boolean
     * @author fzr
     */
    public static Boolean isDelete() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("DELETE");
        }
        return false;
    }

}
