package com.lz.redis.demo.annotatiion;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface ApiVersion {
    int value() default 1;
}
