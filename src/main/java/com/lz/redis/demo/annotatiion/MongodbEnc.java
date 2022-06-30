package com.lz.redis.demo.annotatiion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongodbEnc {
    boolean enableDocument() default true;
    String tableFiled() default "";
    String encType() default "SM4";
}
