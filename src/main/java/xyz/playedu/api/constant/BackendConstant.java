package xyz.playedu.api.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackendConstant {
    public final static String SUPER_ADMIN_ROLE = "super-role";

    public final static List<String> UN_AUTH_URI_WHITELIST = new ArrayList<>() {{
        add("/backend/v1/system/image-captcha");
        add("/backend/v1/auth/login");
    }};

    public final static String RESOURCE_TYPE_VIDEO = "VIDEO";
    public final static String RESOURCE_TYPE_IMAGE = "IMAGE";
    public final static String RESOURCE_TYPE_PDF = "PDF";
    public final static String RESOURCE_TYPE_WORD = "WORD";
    public final static String RESOURCE_TYPE_PPT = "PPT";

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
        put("png", RESOURCE_TYPE_IMAGE);
        put("jpg", RESOURCE_TYPE_IMAGE);
        put("jpeg", RESOURCE_TYPE_IMAGE);
        put("gif", RESOURCE_TYPE_IMAGE);
        put("pdf", RESOURCE_TYPE_PDF);
        put("mp4", RESOURCE_TYPE_VIDEO);
        put("doc", RESOURCE_TYPE_WORD);
        put("docx", RESOURCE_TYPE_WORD);
        put("ppt", RESOURCE_TYPE_PPT);
        put("pptx", RESOURCE_TYPE_PPT);
    }};
    public final static HashMap<String, String> RESOURCE_TYPE_2_DIR = new HashMap<>() {{
        put(RESOURCE_TYPE_VIDEO, UPLOAD_VIDEO_DIR);
        put(RESOURCE_TYPE_IMAGE, UPLOAD_IMAGE_DIR);
        put(RESOURCE_TYPE_PDF, UPLOAD_PDF_DIR);
        put(RESOURCE_TYPE_WORD, UPLOAD_WORD_DIR);
        put(RESOURCE_TYPE_PPT, UPLOAD_PPT_DIR);
    }};

    public final static String STORAGE_DRIVER_MINIO = "minio";

    public final static String[] COURSE_HOUR_TYPE_WHITELIST = {"VIDEO"};
    public final static String[] COURSE_HOUR_TYPE_WHITELIST_TEXT = {"视频"};

    public final static String UPLOAD_IMAGE_DIR = "images/";
    public final static String UPLOAD_VIDEO_DIR = "videos/";
    public final static String UPLOAD_PDF_DIR = "pdf/";
    public final static String UPLOAD_WORD_DIR = "word/";
    public final static String UPLOAD_PPT_DIR = "word/";

    public final static String PRIVACY_FIELD_TYPE_EMAIL = "email";
    public final static String PRIVACY_FIELD_TYPE_PHONE = "phone";
    public final static String PRIVACY_FIELD_TYPE_NAME = "name";
    public final static String PRIVACY_FIELD_TYPE_ID_CARD = "IDCard";

    public final static String APP_CONFIG_FIELD_TYPE_IMAGE = "image";
    public final static String APP_CONFIG_FIELD_TYPE_INPUT = "input";
    public final static String APP_CONFIG_FIELD_TYPE_TEXT = "text";
    public final static String APP_CONFIG_FIELD_TYPE_SELECT = "select";

}
