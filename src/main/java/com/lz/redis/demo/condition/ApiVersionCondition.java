package com.lz.redis.demo.condition;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    /**用于匹配请求url中的版本号**/
    private static final Pattern VERSION_PATTERN = Pattern.compile("v(\\d+)/");

    private int apiVersion;

    public ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**方法覆盖类**/
    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        Matcher matcher = VERSION_PATTERN.matcher(requestURI);
        if(matcher.find()){
            Integer version = Integer.parseInt(matcher.group(1));
            if(version >= this.apiVersion) {
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return apiVersionCondition.getApiVersion()-this.apiVersion;
    }

    private int getApiVersion(){
        return this.apiVersion;
    }
}
