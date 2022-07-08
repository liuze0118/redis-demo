package com.lz.redis.demo.ilicense;

/**
 * @author : liuze
 * @date: 2022/7/1 9:54
 **/
public class DefaultLicenseHandler extends LicenseHandlerTemplate {

    public DefaultLicenseHandler(LicenseVerify licenseVerify) {
        super(licenseVerify);
    }

    @Override
    Boolean checkStrategy() {
        return null;
    }

    @Override
    void handleStrategy() {

    }
}
