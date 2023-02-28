package xyz.playedu.api.constant;

public class BackendConstant {
    public final static String SUPER_ADMIN_ROLE = "super-role";

    public final static String[] UN_AUTH_URI_WHITELIST = {"/backend/v1/system/image-captcha", "/backend/v1/auth/login",};

    public final static String[] RESOURCE_TYPE_WHITELIST = {"IMAGE", "PDF", "VIDEO", "WORD", "PPT"};

    public final static String[] RESOURCE_DISK_WHITELIST = {"MINIO"};

    public final static String[] COURSE_HOUR_TYPE_WHITELIST = {"VIDEO"};
    public final static String[] COURSE_HOUR_TYPE_WHITELIST_TEXT = {"视频"};

    public final static String[] UPLOAD_IMAGE_EXT_WL = {"png", "jpg", "jpeg", "gif"};
    public final static String[] UPLOAD_IMAGE_CONTENT_TYPE_WL = {"image/png", "image/jpg", "image/jpeg", "image/gif"};
    public final static String UPLOAD_IMAGE_DIR = "images/";

}
