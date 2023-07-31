package com.lz.redis.demo.filter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
public class LogHttpFilter extends OncePerRequestFilter {

    private static final long SLOW_RESPONSE = 1000;

    private static final List<String> CAN_LOG_MEDIA_TYPE = Lists.newArrayList(
        // 表单
        MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        // json
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_JSON_UTF8_VALUE,
        // xml
        MediaType.APPLICATION_XML_VALUE,
        MediaType.TEXT_XML_VALUE,
        // text
        MediaType.TEXT_HTML_VALUE,
        MediaType.TEXT_PLAIN_VALUE
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            // 能log的才wrap
            if (CAN_LOG_MEDIA_TYPE.contains(request.getContentType())) {
                request = getRequestWrapper(request);
                response = getResponseWrapper(response);
                logRequest((ContentCachingRequestWrapper) request);
            }
            long startTime = System.currentTimeMillis();
            try {
                filterChain.doFilter(request, response);
            } finally {
                if (CAN_LOG_MEDIA_TYPE.contains(request.getContentType())) {
                    logResponse((ContentCachingResponseWrapper) response);
                }
                if (shouldLogElapsedTime(request)) {
                    logElapsedTime(request, startTime);
                }
            }
        }
    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("FilterLog | api-alias");
            stringBuilder.append("[\"");
            stringBuilder.append(requestWrapper.getRequestURI().replace("/", ""));
            stringBuilder.append("\"]: ");
            stringBuilder.append(getParams(requestWrapper));
            stringBuilder.append(getBody(requestWrapper));
        } catch (Exception exception) {
            log.error("日志打印出错: ", exception);
        } finally {
            log.info(stringBuilder.toString());
        }
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) throws IOException {
        String responseStr = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("response body: {}", responseStr);
        responseWrapper.copyBodyToResponse();
    }

    // 从源Request中获取，是url中的params，不是body中的表单数据
    private String getParams(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> enumeration = requestWrapper.getRequest().getParameterNames();
        if (enumeration.hasMoreElements()) {
            stringBuilder.append("request parameters: ");
        }
        while (enumeration.hasMoreElements()) {
            String paramName = enumeration.nextElement();
            stringBuilder.append(paramName);
            stringBuilder.append(" = ");
            stringBuilder.append(requestWrapper.getRequest().getParameter(paramName));
            stringBuilder.append(", ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), " ; ");
        }
        return stringBuilder.toString();
    }

    private String getBody(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        if (requestWrapper.getMethod().equalsIgnoreCase("POST")) {
            stringBuilder.append("request body: ");
            stringBuilder.append(new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));
        }
        return stringBuilder.toString();
    }

    private void logElapsedTime(HttpServletRequest request, long startTime){
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime < SLOW_RESPONSE) {
            log.info("api elapsed time [{}], path: {}", elapsedTime, request.getRequestURI());
        } else {
            log.warn("api slow response, elapsed time [{}], path: {}", elapsedTime, request.getRequestURI());
        }
    }

    private ContentCachingRequestWrapper getRequestWrapper(HttpServletRequest request) throws IOException {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private ContentCachingResponseWrapper getResponseWrapper(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    protected boolean shouldLogElapsedTime(HttpServletRequest request) {
        // arms也可以做api耗时监控, 所以这里其实可以不用打印耗时
        // TODO 慢响应路径过滤
        return false;
    }
}
