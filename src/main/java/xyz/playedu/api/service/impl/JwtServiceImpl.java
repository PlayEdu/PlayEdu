package xyz.playedu.api.service.impl;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.types.JwtToken;
import xyz.playedu.api.util.ToolUtil;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JWTService {

    @Value("${playedu.jwt.key}")
    private String ConfigKey;

    @Value("${playedu.jwt.expire}")
    private Long ConfigExpire;

    public JwtToken generate(Integer userId, String iss, String prv) {
        long curTime = System.currentTimeMillis();

        JWTPayload payload = new JWTPayload();
        payload.setPrv(prv);
        payload.setIss(iss);
        payload.setJti(ToolUtil.uuid());
        payload.setNbf(curTime);
        payload.setIat(curTime);
        payload.setExp(curTime + ConfigExpire);
        payload.setSub(userId);

        SecretKey key = Keys.hmacShaKeyFor(ConfigKey.getBytes(StandardCharsets.UTF_8));

        JwtBuilder builder = Jwts.builder();
        builder.setId(payload.getJti()).setIssuedAt(new Date(payload.getIat())).claim("prv", payload.getPrv());
        builder.setExpiration(new Date(payload.getExp())).setIssuer(payload.getIss());
        builder.setSubject(String.valueOf(payload.getSub())).setNotBefore(new Date(payload.getNbf()));
        builder.signWith(key);

        JwtToken token = new JwtToken();
        token.setToken(builder.compact());
        token.setExpire(payload.getExp() / 1000);

        return token;
    }

    public JWTPayload parse(String token) {
        return null;
    }

}
