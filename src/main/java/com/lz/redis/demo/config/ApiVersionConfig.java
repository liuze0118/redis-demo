package com.lz.redis.demo.config;

import com.lz.redis.demo.annotatiion.ApiVersion;
import com.lz.redis.demo.condition.ApiVersionCondition;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class ApiVersionConfig extends WebMvcConfigurationSupport {

    @Override
    public RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        CustomRequestMappingHandleMapping handleMapping = new CustomRequestMappingHandleMapping();
        handleMapping.setOrder(0);
        return handleMapping;
    }

    private static class CustomRequestMappingHandleMapping extends RequestMappingHandlerMapping{

        @Override
        protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
            ApiVersion apiVersion = AnnotationUtils.getAnnotation(handlerType,ApiVersion.class);
            return createApiVersionCondition(apiVersion);
        }

        @Override
        protected RequestCondition<?> getCustomMethodCondition(Method method) {
            ApiVersion apiVersion = AnnotationUtils.getAnnotation(method,ApiVersion.class);
            return createApiVersionCondition(apiVersion);
        }

        private RequestCondition<ApiVersionCondition> createApiVersionCondition(ApiVersion apiVersion){
            return apiVersion == null? null:new ApiVersionCondition(apiVersion.value());
        }

    }
}
