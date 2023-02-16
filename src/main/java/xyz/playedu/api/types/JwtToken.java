package xyz.playedu.api.types;

import lombok.Data;

@Data
public class JwtToken {

    private String token;

    private Long expire;

}
