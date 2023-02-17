package xyz.playedu.api.service;

import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JwtToken;

public interface JWTService {
    JwtToken generate(Integer userId, String iss, String prv);

    boolean isInBlack(String jti);

    void logout(String token, String prv) throws JwtLogoutException;

    JWTPayload parse(String token, String prv) throws JwtLogoutException;
}
