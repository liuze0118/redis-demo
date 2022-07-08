package com.lz.redis.demo.ilicense;

import de.schlichtherle.license.AbstractKeyStoreParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    private String storePath;
    private String alias;
    private String storePwd;
    private String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }


    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getStorePwd() {
        return this.storePwd;
    }

    @Override
    public String getKeyPwd() {
        return this.keyPwd;
    }

    /**
     * AbstractKeyStoreParam里面的getStream()方法默认文件是存储的项目中。
     * 用于将公私钥存储文件存放到其他磁盘位置而不是项目中
     */
    @Override
    public InputStream getStream() throws IOException {
        File file;
        if(!storePath.startsWith("C")){
            Resource resource = new ClassPathResource(storePath);
            file  = resource.getFile();
        }else {
            file = ResourceUtils.getFile(storePath);
        }
        if (file.exists()) {
            return new FileInputStream(file);
        } else {
            throw new FileNotFoundException(storePath);
        }
    }

}
