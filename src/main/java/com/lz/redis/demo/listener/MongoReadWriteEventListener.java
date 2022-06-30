package com.lz.redis.demo.listener;

import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.utils.SM4Utils;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@Slf4j
public class MongoReadWriteEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        if (event.getSource() != null) {
            ReflectionUtils.doWithFields(event.getSource().getClass(), field -> {
                // @MongodbEnc注解字段，写库前做SM4加密
                if (field.isAnnotationPresent(MongodbEnc.class)) {
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(event.getSource());
                    if (value != null) {
                        String ciphertext = (String) value;
                        if (StringUtils.isNotBlank(ciphertext)) {
                            String plainText = "123456";
                            log.info("解密前：{}，解密后：{}", ciphertext, plainText);
                            field.set(event.getSource(), plainText);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        if (event.getSource() != null && event.getSource().getClass().isAnnotationPresent(MongodbEnc.class)) {
            ReflectionUtils.doWithFields(event.getSource().getClass(), field -> {
                // @MongodbEnc注解字段，读库后做SM4解密
                if (field.isAnnotationPresent(MongodbEnc.class)) {
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(event.getSource());
                    if (value != null) {
                        String plainText = (String) value;
                        String cipherText = SM4Utils.decryptSm4(plainText);
                        log.info("解密前:{}，解密后:{}", plainText, cipherText);
                        field.set(event.getSource(), cipherText);
                    }
                }
            });
        }
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        super.onBeforeSave(event);
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        Document source = event.getSource();
        super.onAfterLoad(event);
    }

    public MongoReadWriteEventListener() {
        super();
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        super.onAfterSave(event);
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
        super.onAfterDelete(event);
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        super.onBeforeDelete(event);
    }
}
