package com.lz.redis.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 
 * @Author: liuze
 * @Date: 2022/6/29
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {


    private static final String uploadFileType = "StandardMultipartFile";


    @PostConstruct
    public void init(){
        log.info("初始化日志切面-------------");
    }

    @Pointcut("@annotation(com.lz.redis.demo.annotatiion.SysLog)")
    public void sysLogPt(){

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController)")
    public void webLog() {

    }
    @Pointcut("execution(public * com.lz.redis.demo.controller..*(..))")
    public void controller() {

    }
    @Around("sysLogPt()")
    public void doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("----------sys---------log-------------");
        joinPoint.proceed();
    }


    @Before("controller()")
    public void doBefore(JoinPoint joinPoint){
        // 开始打印请求日志
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Map<String, Map> headerParams = buildHeaderParam(request);

                Map<String, Map> reqParams = buildRequestParam(request);
                String requestPath = request.getRequestURI();
                Map<String, Object> params = new HashMap<>();
                Enumeration<String> it = request.getParameterNames();
                //
                int a = 0;
                // 构造uri及参数params_map
                while (it.hasMoreElements()) {
                    String key = it.nextElement();
                    String value = request.getParameter(key);
                    a++;
                    if (value != null) {
                        if (a == 1) {
                            requestPath += "?" + key + "=" + value;
                        } else {
                            requestPath += "&" + key + "=" + value;
                        }
                        params.put(key, value);
                    }
                }
                // 保留路径参数
                Object[] args = joinPoint.getArgs();
                //log.debug("arg: [{}]", args);
                for (Object arg : args) {
                    if (arg == null || arg == "" || arg instanceof String || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                        continue;
                    }
                    //log.debug("arg: " + arg);
                    params.put(arg.getClass().getSimpleName(), arg);
                }
                //log.debug("headerParams: [{}]", headerParams);
                //log.debug("reqParams: [{}]", reqParams);
                //log.debug("params: [{}]", params);
                reqParams.forEach((k, map) -> map.forEach((kk, v) -> {
                    params.remove(kk);
                }));

                // 从params_map参数中移除headerParams
                headerParams.forEach((k, map) -> map.forEach((kk, v) -> {
                    params.remove(k);
                }));
                //log.debug("bodyParams:[{}]", params);
                Map<String, Object> allParams = new HashMap<>(3);
                allParams.putAll(reqParams);
                allParams.putAll(headerParams);
                allParams.putAll(params);
                initSysLog(joinPoint,request,params);
                if (params.containsKey(uploadFileType)) {
                    log.debug("文件上传请求，不打印参数");
                    log.info("===START===:");
                    return;
                }
                log.debug("request start: [{}]", JSON.toJSONString(allParams));
                log.info("{}===START===: 参数[{}]", "**", JSON.toJSONString(allParams));
                log.debug("requestPath: [{}]", requestPath);
            }
        } catch (Exception e) {
            log.error("打印请求日志失败",e);
        }

    }

    private void initSysLog(JoinPoint joinPoint, HttpServletRequest request, Map<String, Object> params) {
        try {
//            SysLog sysLog = new SysLog();
//            // 打印请求相关参数
//            log.debug("===============================正常请求打印参数开始================================================");
//            // 打印请求 url
//            log.debug("URL            : {}", request.getRequestURI());
//            sysLog.setRequestUrl(request.getRequestURI());
//            // 打印 Http method
//            log.debug("HTTP Method    : {}", request.getMethod());
//            sysLog.setHttpMethod(request.getMethod());
//            // 打印调用 controller 的全路径以及执行方法
//            log.debug("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
//            sysLog.setMethodName(joinPoint.getSignature().getDeclaringTypeName()+","+joinPoint.getSignature().getName());
//            // 打印请求的 IP
//            log.debug("IP             : {}", request.getRemoteAddr());
//            sysLog.setIpAddress(request.getRemoteAddr());
//            // 打印请求入参
//            if(joinPoint.getArgs()!=null) {
//                if (params.containsKey(uploadFileType)) {
//                    log.debug("文件上传请求，不打印参数");
//                }else{
//                    log.debug("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
//                    sysLog.setParams(JSON.toJSONString(joinPoint.getArgs()));
//                }
//            }
//            sysLog.setRequestId(MDC.get(SystemConstant.REQUEST_ID));
//            sysLog.setCreateTime(new Date());
//            sysLog.setTenantId(AuthUtil.getTenantId());
//            sysLogService.save(sysLog);
        } catch (Exception e) {
            log.error("系统日志入库失败",e);
        }
    }

    private Map buildHeaderParam(HttpServletRequest req) {
        List<String> headerList = Lists.newArrayList("Authentication");
        Map<String, String> headerParams = new HashMap<>();
        StringBuilder headerSb = new StringBuilder();
        Enumeration enu = req.getHeaderNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            if (headerList.contains(paraName)) {
                headerSb.append(paraName).append("=").append(req.getHeader(paraName)).append(",");
                headerParams.put(paraName, req.getHeader(paraName));
            }
        }
        log.debug("{}HeaderParams:[{}]", "|**", headerSb);
        // return headerParams;
        HashMap<String, Map> headerMap = new HashMap<>();
        headerMap.put("headerParams", headerParams);
        return headerMap;
    }

    private HashMap<String, Map> buildRequestParam(HttpServletRequest request) {
        if (isUploadReq(request)) {
            StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
            request = resolver.resolveMultipart(request);
        }
        StringBuilder sb = new StringBuilder();
        Map<String, String> params = new HashMap<>();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            if ("uploadFile".equals(paraName)) {
                continue;
            }
            sb.append(paraName).append("=").append(request.getParameter(paraName)).append(",");
            params.put(paraName, request.getParameter(paraName));
        }
        log.debug("{}RequestParams:[{}]", "|**", sb);
        HashMap<String, Map> reqParams = new HashMap<>();
        reqParams.put("reqParams", params);
        return reqParams;
    }

    private boolean isUploadReq(HttpServletRequest request) {
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            if ("uploadFile".equals(paraName)) {
                return true;
            }
        }
        return false;
    }
}
