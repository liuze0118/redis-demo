package com.lz.redis.demo.contorller;

import com.lz.redis.demo.ilicense.ILicenseCreator;
import com.lz.redis.demo.ilicense.License;
import com.lz.redis.demo.ilicense.LicenseVerify;
import de.schlichtherle.license.LicenseContentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : liuze
 * @date: 2022/6/30 15:14
 **/
@Slf4j
@RestController
@RequestMapping("/license")
public class LicenseController {

    //this is ----dev

    @Resource
    private LicenseVerify licenseVerify;

    @RequestMapping("/create")
    @ResponseBody
    public Map<String,Object> createLicense(@RequestBody License license){
        HashMap<String, Object> resultMap = new HashMap<>();
        log.info("初始化证书创建器");
        ILicenseCreator ILicenseCreator = new ILicenseCreator(license);
        // 生成license
        log.info("开始生成证书");
        boolean result = ILicenseCreator.generateLicense();
        if(result){
            resultMap.put("result","OK");
            resultMap.put("message",license);
        }else{
            resultMap.put("result","FAIL");
            resultMap.put("message","证书创建失败");
        }
        return resultMap;
    }

    @RequestMapping("/check")
    public String createLicense() throws LicenseContentException {
        boolean verify = licenseVerify.verify();
        return verify+"";
    }

    @RequestMapping("/renewal")
    public String licenseRenewal(){

        return "OK";
    }

    @RequestMapping("/install")
    public String licenseInstall(){
        licenseVerify.installLicense();
        return "OK";
    }
}
