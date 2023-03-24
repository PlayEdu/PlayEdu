package xyz.playedu.api.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 14:07
 */
public class FrontendConstant {

    public final static List<String> UN_AUTH_URI_WHITELIST = new ArrayList<>() {{
        add("/api/v1/system/config");
        add("/api/v1/system/image-captcha");
        add("/api/v1/auth/login/password");
    }};


    public final static String USER_UPLOAD_IMAGE_TYPE_AVATAR = "avatar";

    public final static String USER_UPLOAD_IMAGE_SCENE_AVATAR = "avatar";

    public final static String DIR_AVATAR = "user/avatar/";

}
