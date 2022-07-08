package com.lz.redis.demo.ilicense;

/**
 * @author : liuze
 * @date: 2022/7/1 9:51
 **/
public abstract class LicenseHandlerTemplate {

    private LicenseVerify licenseVerify;

    public LicenseHandlerTemplate(LicenseVerify licenseVerify) {
        this.licenseVerify = licenseVerify;
    }

//    public boolean verify(){
//        return this.licenseVerify.verify();
//    }

    abstract Boolean checkStrategy();

    abstract void handleStrategy();

}
