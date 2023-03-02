package xyz.playedu.api.constant;

import java.util.HashMap;

public class BackendConstant {
    public final static String SUPER_ADMIN_ROLE = "super-role";

    public final static String[] UN_AUTH_URI_WHITELIST = {"/backend/v1/system/image-captcha", "/backend/v1/auth/login",};

    public final static String[] RESOURCE_TYPE_WHITELIST = {"IMAGE", "PDF", "VIDEO", "WORD", "PPT"};
    public final static HashMap<String, String> RESOURCE_EXT_2_CONTENT_TYPE = new HashMap<>() {{
        put("png", "image/png");
        put("jpg", "image/jpg");
        put("jpeg", "image/jpeg");
        put("gif", "image/gif");
        put("pdf", "application/pdf");
        put("mp4", "video/mp4");
        put("doc", "application/msword");
        put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("ppt", "application/vnd.ms-powerpoint");
        put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }};
    public final static HashMap<String, String> RESOURCE_EXT_2_TYPE = new HashMap<>() {{
        put("png", "IMAGE");
        put("jpg", "IMAGE");
        put("jpeg", "IMAGE");
        put("gif", "IMAGE");
        put("pdf", "PDF");
        put("mp4", "VIDEO");
        put("doc", "WORD");
        put("docx", "WORD");
        put("ppt", "PPT");
        put("pptx", "PPT");
    }};

    public final static String[] RESOURCE_DISK_WHITELIST = {"MINIO"};

    public final static String[] COURSE_HOUR_TYPE_WHITELIST = {"VIDEO"};
    public final static String[] COURSE_HOUR_TYPE_WHITELIST_TEXT = {"视频"};

    // 图片上传相关配置
    public final static String[] UPLOAD_IMAGE_EXT_WL = {"png", "jpg", "jpeg", "gif"};
    public final static String[] UPLOAD_IMAGE_CONTENT_TYPE_WL = {"image/png", "image/jpg", "image/jpeg", "image/gif"};
    public final static String UPLOAD_IMAGE_DIR = "images/";

}
