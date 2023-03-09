package xyz.playedu.api.request.backend;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/9 16:13
 */
@Data
public class AppConfigRequest {

    private HashMap<String, String> data;

}
