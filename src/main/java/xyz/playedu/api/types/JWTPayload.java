package xyz.playedu.api.types;

import lombok.Data;

import java.util.HashMap;

/**
 * @see https://www.rfc-editor.org/rfc/rfc7519#section-4.1
 */
@Data
public class JWTPayload {

    /**
     * subject
     */
    private Integer sub;

    /**
     * Issued At
     */
    private Long iat;

    /**
     * Expiration Time
     */
    private Long exp;

    /**
     * Not Before
     */
    private Long nbf;

    /**
     * JWT ID
     */
    private String jti;

    /**
     * Issuer
     */
    private String iss;

    /**
     * Payload
     */
    private String prv;

    private HashMap<String, Object> claims;

}
