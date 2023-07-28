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
package xyz.playedu.api.aspectj;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.api.annotation.Log;
import xyz.playedu.api.domain.AdminLog;
import xyz.playedu.api.service.AdminLogService;
import xyz.playedu.api.service.BackendAuthService;
import xyz.playedu.api.util.IpUtil;
import xyz.playedu.api.util.RequestUtil;
import xyz.playedu.api.util.StringUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AdminLogAspect {

    @Autowired private BackendAuthService authService;

    @Autowired private AdminLogService adminLogService;

    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = {
        "password", "oldPassword", "newPassword", "confirmPassword"
    };

    /** Controller层切点 注解拦截 */
    @Pointcut("@annotation(xyz.playedu.api.annotation.Log)")
    public void logPointCut() {}

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获取注解信息
            Log controllerLog = getAnnotationLog(joinPoint);
            if (null == controllerLog) {
                return;
            }

            // 日志
            AdminLog adminLog = new AdminLog();
            adminLog.setAdminId(authService.userId());
            adminLog.setModule("BACKEND");
            adminLog.setTitle(controllerLog.title());
            adminLog.setOpt(controllerLog.businessType().ordinal());

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            adminLog.setMethod(className + "." + methodName + "()");

            HttpServletRequest request = RequestUtil.handler();
            if (null == request) {
                return;
            }
            adminLog.setRequestMethod(request.getMethod());
            adminLog.setUrl(request.getRequestURL().toString());
            String params = "";
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (StringUtil.isNotEmpty(parameterMap)) {
                params = JSONUtil.toJsonStr(parameterMap);
            } else {
                Object[] args = joinPoint.getArgs();
                if (StringUtil.isNotNull(args)) {
                    params = StringUtil.arrayToString(args);
                }
            }
            if (StringUtil.isNotEmpty(params)) {
                JSONObject paramObj = JSONUtil.parseObj(params);
                for (String i : Arrays.asList(EXCLUDE_PROPERTIES)) {
                    if (paramObj.containsKey(i)) {
                        paramObj.put(i, "******");
                    }
                }
                adminLog.setParam(StringUtils.substring(JSONUtil.toJsonStr(paramObj), 0, 2000));
            }
            adminLog.setResult(JSONUtil.toJsonStr(jsonResult));
            adminLog.setIp(IpUtil.getIpAddress());
            adminLog.setIpArea(IpUtil.getRealAddressByIP(IpUtil.getIpAddress()));

            if (null != e) {
                adminLog.setErrorMsg(StringUtil.substring(e.getMessage(), 0, 2000));
            }
            adminLog.setCreatedAt(new Date());
            // 保存数据库
            adminLogService.save(adminLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:" + exp.getMessage(), e);
        }
    }

    /** 是否存在注解，如果存在就获取 */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
