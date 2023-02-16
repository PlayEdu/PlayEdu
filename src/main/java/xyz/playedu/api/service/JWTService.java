package xyz.playedu.api.service;

import xyz.playedu.api.types.JwtToken;

public interface JWTService {
    JwtToken generate(Integer userId, String iss, String prv);
}
