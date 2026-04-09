package xyz.playedu.common.service;

import xyz.playedu.common.types.LdapConfig;

import java.util.Map;

public interface EnterpriseIntegrationService {

    // 企业微信集成
    Map<String, Object> wechatWorkLogin(String code);
    void sendWechatWorkMessage(String userId, String message);
    Map<String, Object> getWechatWorkUserInfo(String userId);

    // 钉钉集成
    Map<String, Object> dingTalkLogin(String code);
    void sendDingTalkMessage(String userId, String message);
    Map<String, Object> getDingTalkUserInfo(String userId);

    // LDAP集成
    Map<String, Object> ldapLogin(String username, String password);
    void syncLdapUsers();
    void syncLdapDepartments();

    // SSO单点登录
    String generateSsoUrl(String redirectUri);
    Map<String, Object> ssoCallback(String code);

    // HR系统集成
    void syncHrUsers();
    void syncHrDepartments();
    Map<String, Object> getHrUserInfo(String userId);

    // API开放平台
    String generateApiKey(String appName);
    void revokeApiKey(String apiKey);
    Map<String, Object> validateApiKey(String apiKey);
}
