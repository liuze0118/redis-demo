package com.lz.redis.demo.ilicense;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Data
@Slf4j
public class License {
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 私钥别称
     */
    private String privateAlias;

    /**
     * 私钥密码（需要妥善保管，不能让使用者知道）
     */
    private String keyPass;

    /**
     * 访问私钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 私钥库存储路径
     */
    private String privateKeysStorePath;

    /**
     * 证书生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issuedTime = new Date();

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 用户类型
     */
    private String consumerType = "user";

    /**
     * 用户数量
     */
    private Integer consumerAmount = 1;

    /**
     * 描述信息
     */
    private String description = "jkyy license授权";

    /**
     * 额外的服务器硬件校验信息
     */
    private LicenseExtraModel licenseExtraModel;

    public static void createLicenseCert() {

        // 生成license需要的一些参数
        License param = new License();
        // 证书授权主体
        param.setSubject("jiangkanyouyi");
        // 私钥别名
        param.setPrivateAlias("privateKey");
        // 私钥密码（需要妥善保管，不能让使用者知道）
        param.setKeyPass("liu123");
        // 访问私钥库的密码
        param.setStorePass("liu123");
        // 证书存储地址
        param.setLicensePath("C:\\AppData\\license\\license.lic");
        // 私钥库所在地址
        param.setPrivateKeysStorePath("C:\\AppData\\license\\privateKeys.stroe");
        // 证书生效时间
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        // 证书失效时间
        Calendar expiryCalendar = Calendar.getInstance();
        // 设置当前时间
        expiryCalendar.setTime(new Date());
        // 往后延长一年 = 授权一年时间
        expiryCalendar.add(Calendar.MINUTE,20);
        param.setExpiryTime(expiryCalendar.getTime());
        // 用户类型
        param.setConsumerType("user");
        // 用户数量
        param.setConsumerAmount(1);
        // 描述
        param.setDescription("测试");
        log.info("初始化证书创建器");
        System.out.println(JSON.toJSONString(param));
//        ILicenseCreator ILicenseCreator = new ILicenseCreator(param);
//        // 生成license
//        log.info("开始生成证书");
//        ILicenseCreator.generateLicense();
    }

    public static void main(String[] args) {
        createLicenseCert();
    }

}
