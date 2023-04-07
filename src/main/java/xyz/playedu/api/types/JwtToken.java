/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.types;

import lombok.Data;

@Data
public class JwtToken {

    private String token;

    private Long expire;
}
