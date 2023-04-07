/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JwtToken;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.RedisUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Slf4j
@Service
public class JwtServiceImpl implements JWTService {

    @Value("${playedu.jwt.key}")
    private String ConfigKey;

    @Value("${playedu.jwt.expire}")
    private Long ConfigExpire;

    @Value("${playedu.jwt.cache-black-prefix}")
    private String ConfigCacheBlackPrefix;

    public JwtToken generate(Integer userId, String iss, String prv) {
        long curTime = System.currentTimeMillis();

        JWTPayload payload = new JWTPayload();
        payload.setPrv(prv);
        payload.setIss(iss);
        payload.setJti(HelperUtil.uuid());
        payload.setNbf(curTime);
        payload.setIat(curTime);
        payload.setExp(curTime + ConfigExpire * 1000);
        payload.setSub(userId);

        JwtBuilder builder = Jwts.builder();
        builder.setId(payload.getJti())
                .setIssuedAt(new Date(payload.getIat()))
                .claim("prv", payload.getPrv());
        builder.setExpiration(new Date(payload.getExp())).setIssuer(payload.getIss());
        builder.setSubject(String.valueOf(payload.getSub()))
                .setNotBefore(new Date(payload.getNbf()));
        builder.signWith(getSecretKey());

        JwtToken token = new JwtToken();
        token.setToken(builder.compact());
        token.setExpire(payload.getExp() / 1000);

        return token;
    }

    public JWTPayload parse(String token, String prv) throws JwtLogoutException {
        Claims claims = parseToken(token, prv);
        JWTPayload payload = new JWTPayload();

        payload.setSub(Integer.valueOf(claims.getSubject()));
        payload.setIss(claims.getIssuer());
        payload.setPrv((String) claims.get("prv"));
        payload.setNbf(claims.getNotBefore().getTime());
        payload.setExp(claims.getExpiration().getTime());
        payload.setIat(claims.getIssuedAt().getTime());
        payload.setJti(claims.getId());

        return payload;
    }

    public boolean isInBlack(String jti) {
        return RedisUtil.exists(getBlackCacheKey(jti));
    }

    public void logout(String token, String prv) throws JwtLogoutException {
        Claims claims = parseToken(token, prv);
        String cacheKey = getBlackCacheKey(claims.getId());
        Long expire = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
        RedisUtil.set(cacheKey, 1, expire);
    }

    @Override
    public void userLogout(String token) throws JwtLogoutException {
        logout(token, SystemConstant.JWT_PRV_USER);
    }

    @Override
    public void adminUserLogout(String token) throws JwtLogoutException {
        logout(token, SystemConstant.JWT_PRV_ADMIN_USER);
    }

    private Claims parseToken(String token, String prv) throws JwtLogoutException {
        Claims claims =
                (Claims)
                        Jwts.parserBuilder()
                                .setSigningKey(getSecretKey())
                                .require("prv", prv)
                                .build()
                                .parse(token)
                                .getBody();
        if (isInBlack(claims.getId())) {
            throw new JwtLogoutException();
        }
        return claims;
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(ConfigKey.getBytes(StandardCharsets.UTF_8));
    }

    private String getBlackCacheKey(String jti) {
        return ConfigCacheBlackPrefix + jti;
    }
}
