package xyz.playedu.api.types;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 18:43
 */
@Data
public class SelectOption<T> {
    private String key;
    private T value;
}
