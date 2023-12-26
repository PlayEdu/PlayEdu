/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.util.ldap;

import lombok.extern.slf4j.Slf4j;

import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.util.StringUtil;

import java.io.IOException;
import java.util.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.*;

@Slf4j
public class LdapUtil {

    // person,posixAccount,inetOrgPerson,organizationalPerson => OpenLDAP的属性
    // user => Window AD 域的属性
    private static final String USER_OBJECT_CLASS =
            "(|(objectClass=person)(objectClass=posixAccount)(objectClass=inetOrgPerson)(objectClass=organizationalPerson)(objectClass=user))";

    private static final String[] USER_RETURN_ATTRS =
            new String[]{
                    // OpenLDAP 的属性
                    "uid", // 用户的唯一识别符号，全局唯一，可以看做用户表的手机号，此字段可用于配合密码直接登录
                    "cn", // CommonName -> 可以认作为人的名字，比如：张三。在LDAP中此字段是可以重复的,但是同一ou下不可重复
                    "email", // 邮箱，同上
                    "entryUUID",

                    // Window AD 域的属性
                    "name",
                    "userPrincipalName",
                    "distinguishedName",
                    "sAMAccountName",
                    "displayName",
                    "uSNCreated", // AD域的唯一属性

                    // 公用属性
                    "mail",
            };
    private static final String[] OU_RETURN_ATTRS = new String[]{"ou", "usncreated"};

    public static LdapContext initContext(String url, String adminUser, String adminPass)
            throws NamingException {
        Hashtable<String, String> context = new Hashtable<>();
        context.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        context.put(Context.SECURITY_AUTHENTICATION, "simple");
        // 服务地址
        context.put(Context.PROVIDER_URL, url);
        // 管理员账户和密码
        context.put(Context.SECURITY_PRINCIPAL, adminUser);
        context.put(Context.SECURITY_CREDENTIALS, adminPass);
        return new InitialLdapContext(context, null);
    }

    public static List<LdapTransformUser> users(
            String url, String adminUser, String adminPass, String baseDN) throws NamingException, IOException {
        LdapContext ldapContext = initContext(url, adminUser, adminPass);

        int pageSize = 1000;
        List<LdapTransformUser> users = new ArrayList<>();

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(USER_RETURN_ATTRS);
        controls.setReturningObjFlag(true);

        byte[] cookie = null;

        while (true) {
            try {
                if (cookie != null) {
                    ldapContext.setRequestControls(new Control[]{
                            new PagedResultsControl(pageSize, cookie, false),
                    });
                } else {
                    ldapContext.setRequestControls(new Control[]{
                            new PagedResultsControl(pageSize, false)
                    });
                }

                NamingEnumeration<SearchResult> result = ldapContext.search(baseDN, USER_OBJECT_CLASS, controls);
                while (result.hasMoreElements()) {
                    SearchResult item = result.nextElement();
                    if (item != null) {
                        LdapTransformUser ldapTransformUser = parseTransformUser(item, baseDN);
                        users.add(ldapTransformUser);
                    }
                }

                cookie = parseCookie(ldapContext.getResponseControls());
                if (cookie == null || cookie.length == 0) {
                    break;
                }
            } catch (NamingException e) {
                log.error("LDAP用户查询失败", e);
                break;
            }
        }

        closeContext(ldapContext);

        if (users.isEmpty()) {
            log.info("LDAP服务中没有用户");
            return null;
        }

        return users;
    }

    private static byte[] parseCookie(Control[] controls) throws NamingException {
        if (controls != null) {
            for (Control control : controls) {
                if (control instanceof PagedResultsResponseControl) {
                    return ((PagedResultsResponseControl) control).getCookie();
                }
            }
        }
        return null;
    }

    public static List<LdapTransformDepartment> departments(
            String url, String adminUser, String adminPass, String baseDN) throws NamingException {
        LdapContext ldapContext = initContext(url, adminUser, adminPass);

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(OU_RETURN_ATTRS);
        controls.setReturningObjFlag(true);

        String filter = "(objectClass=organizationalUnit)";
        NamingEnumeration<SearchResult> result = null;
        try {
            result = ldapContext.search(baseDN, filter, controls);
        } catch (NamingException e) {
            log.error("LDAP部门查询失败", e);
        } finally {
            closeContext(ldapContext);
        }

        if (result == null || !result.hasMoreElements()) {
            log.info("LDAP部门为空");
            return null;
        }

        // baseDN中的ou作用域
        String ouScopesStr = baseDNOuScope(baseDN);

        List<LdapTransformDepartment> units = new ArrayList<>();
        while (result.hasMoreElements()) {
            SearchResult item = result.nextElement();
            if (item == null) {
                continue;
            }
            Attributes attributes = item.getAttributes();
            if (attributes == null) {
                continue;
            }

            // 唯一特征值
            String uSNCreated = getAttribute(attributes, "uSNCreated");
            if (StringUtil.isEmpty(uSNCreated)) {
                continue;
            }

            // 组织DN
            String name = item.getName();
            if (name.isEmpty()) {
                name = ouScopesStr;
            } else {
                name = name + (ouScopesStr.isEmpty() ? "" : "," + ouScopesStr);
            }

            // 将DN反转
            List<String> tmp = new ArrayList<>(List.of(name.split(",")));
            Collections.reverse(tmp);
            name = String.join(",", tmp);

            LdapTransformDepartment ldapDepartment = new LdapTransformDepartment();
            ldapDepartment.setUuid(uSNCreated);
            ldapDepartment.setDn(name.toLowerCase());

            units.add(ldapDepartment);
        }

        return units;
    }

    public static LdapTransformUser loginByMailOrUid(
            String url,
            String adminUser,
            String adminPass,
            String baseDN,
            String mail,
            String uid,
            String password)
            throws ServiceException, NamingException {
        if (StringUtil.isEmpty(mail) && StringUtil.isEmpty(uid)) {
            throw new ServiceException("mail和Uid不能同时为空");
        }

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(USER_RETURN_ATTRS);
        controls.setReturningObjFlag(true);
        controls.setCountLimit(1);

        String userFilter = "";
        if (StringUtil.isNotEmpty(mail)) {
            userFilter =
                    String.format("(|(mail=%s)(email=%s)(userPrincipalName=%s))", mail, mail, mail);
        } else if (StringUtil.isNotEmpty(uid)) {
            userFilter = String.format("(|(uid=%s)(sAMAccountName=%s))", uid, uid);
        }

        String filter = String.format("(&%s%s)", userFilter, USER_OBJECT_CLASS);

        LdapContext ldapContext = initContext(url, adminUser, adminPass);
        NamingEnumeration<SearchResult> result = null;
        try {
            result = ldapContext.search(baseDN, filter, controls);
        } catch (NamingException e) {
            log.error("LDAP-通过mail或uid登录失败", e);
        } finally {
            closeContext(ldapContext);
        }

        if (result == null || !result.hasMoreElements()) {
            log.info("LDAP-用户不存在");
            return null;
        }

        // 根据mail或uid查询出来的用户
        LdapTransformUser ldapUser = parseTransformUser(result.nextElement(), baseDN);
        if (ldapUser == null) {
            log.info("LDAP-用户不存在");
            return null;
        }

        // 使用用户dn+提交的密码去登录ldap系统
        // 登录成功则意味着密码正确
        // 登录失败则意味着密码错误
        try {
            ldapContext = initContext(url, ldapUser.getDn() + "," + baseDN, password);
            return ldapUser;
        } catch (Exception e) {
            // 无法登录->密码错误
            log.error("LDAP-登录失败", e);
            return null;
        } finally {
            ldapContext.close();
        }
    }

    private static LdapTransformUser parseTransformUser(SearchResult item, String baseDN)
            throws NamingException {
        Attributes attributes = item.getAttributes();
        if (attributes == null) {
            return null;
        }

        LdapTransformUser ldapUser = new LdapTransformUser();
        ldapUser.setDn(item.getName());

        // name解析
        String displayName = getAttribute(attributes, "displayName");
        if (StringUtil.isEmpty(displayName)) {
            displayName = getAttribute(attributes, "cn");
        }
        ldapUser.setCn(displayName);

        // 邮箱解析
        String email = getAttribute(attributes, "mail");
        if (StringUtil.isEmpty(email)) {
            getAttribute(attributes, "email");
        }
        ldapUser.setEmail(email);

        if (attributes.get("uSNCreated") != null) {
            // Window AD域
            ldapUser.setId((String) attributes.get("uSNCreated").get());
            ldapUser.setUid((String) attributes.get("sAMAccountName").get());
        } else {
            // OpenLDAP
            ldapUser.setId((String) attributes.get("entryUUID").get());
            ldapUser.setUid((String) attributes.get("uid").get());
        }

        // ou计算
        String baseDNOuScope = baseDNOuScope(baseDN);
        String[] rdnList =
                (baseDNOuScope.isEmpty()
                        ? ldapUser.getDn().toLowerCase()
                        : ldapUser.getDn().toLowerCase() + "," + baseDNOuScope)
                        .split(",");
        List<String> ou = new ArrayList<>();
        for (String s : rdnList) {
            if (StringUtil.startsWith(s, "ou=")) {
                ou.add(s.replace("ou=", ""));
            }
        }
        Collections.reverse(ou);
        ldapUser.setOu(ou);

        return ldapUser;
    }

    private static String getAttribute(Attributes attributes, String keyName)
            throws NamingException {
        Attribute attribute = attributes.get(keyName);
        if (attribute == null) {
            return null;
        }
        return (String) attribute.get();
    }

    private static String baseDNOuScope(String baseDN) {
        List<String> ouScopes = new ArrayList<>();
        String[] rdnList = baseDN.toLowerCase().split(",");
        for (String s : rdnList) {
            if (s.startsWith("ou=")) {
                ouScopes.add(s);
            }
        }
        return String.join(",", ouScopes);
    }

    private static void closeContext(LdapContext ldapCtx) {
        if (ldapCtx == null) {
            return;
        }
        try {
            ldapCtx.close();
        } catch (NamingException e) {
            log.error("LDAP-资源释放失败", e);
        }
    }
}
