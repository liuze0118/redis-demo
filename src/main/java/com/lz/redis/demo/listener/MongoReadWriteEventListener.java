package com.lz.redis.demo.listener;


import com.lz.redis.demo.annotatiion.MongodbEnc;
import com.lz.redis.demo.utils.SM4Utils;
import com.lz.redis.demo.vo.BaseMongoEntity;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@Slf4j
public class MongoReadWriteEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Document document = event.getDocument();
        if (event.getSource() != null && event.getSource().getClass().isAnnotationPresent(MongodbEnc.class)) {
            doEnc(event.getSource());
        }
    }

    private void doEnc(Object source) {
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            // @MongodbEnc注解字段，写库前做SM4加密
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(source);
            if (field.isAnnotationPresent(MongodbEnc.class)) {
                MongodbEnc annotation = field.getAnnotation(MongodbEnc.class);
                if (value != null) {
                    String cipher;
                    if(annotation.javaType().equals("BigDecimal")){
                        cipher  = value.toString();
                    }else{
                        cipher = (String) value;
                    }
                    if (StringUtils.isNotBlank(cipher)) {
                        String plainText = SM4Utils.encryptSm4(cipher);
                        log.info("加密前：{}，加密后：{}", cipher, plainText);
                        Class<?> type = field.getType();
                        field.set(source, plainText);
                    }
                }
            }else if (value != null && field.getType().getSuperclass() == BaseMongoEntity.class && field.getType().isAnnotationPresent(MongodbEnc.class)){
                doEnc(value);
            }
        });
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        if (event.getSource() != null && event.getSource().getClass().isAnnotationPresent(MongodbEnc.class)) {
           doDec(event.getSource());
        }
    }

    private void doDec(Object source) {
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            // @MongodbEnc注解字段，读库后做SM4解密
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(source);
            if (field.isAnnotationPresent(MongodbEnc.class)) {
                if (value != null) {
                    String cipher = (String) value;
                    if (StringUtils.isNotBlank(cipher)) {
                        String plainText = (String) value;
                        String cipherText = SM4Utils.decryptSm4(plainText);
                        log.info("解密前:{}，解密后:{}", plainText, cipherText);
                        field.set(source, cipherText);
                    }
                }
            }else if (value != null && field.getType().getSuperclass() == BaseMongoEntity.class && field.getType().isAnnotationPresent(MongodbEnc.class)){
                doDec(value);
            }
        });
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
