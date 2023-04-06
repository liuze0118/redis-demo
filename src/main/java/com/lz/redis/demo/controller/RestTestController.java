package com.lz.redis.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.redis.demo.annotatiion.ApiVersion;
import com.lz.redis.demo.dao.UserDao;
import com.lz.redis.demo.download.DownLoadFromUrl;
import com.lz.redis.demo.model.entity.mysql.DataSence;
import com.lz.redis.demo.model.entity.mysql.HealthInterfaceInfo;
import com.lz.redis.demo.service.DataSenceInterfaceInfoService;
import com.lz.redis.demo.service.HealthInterfaceInfoService;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.service.impl.MatchResultServiceImpl;
import com.lz.redis.demo.utils.OssUtils;
import com.lz.redis.demo.utils.PoiExcelUtils;
import com.lz.redis.demo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/{version}/rest")
@Slf4j
public class RestTestController {
    @Autowired
    private UserService userService;
    @Autowired
    private DownLoadFromUrl downLoadFromUrl;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HealthInterfaceInfoService interfaceInfoService;
    @Autowired
    private DataSenceInterfaceInfoService dataSenceInterfaceInfoService;
    @Value("${java.home}")
    private String javahome;
    @Autowired
    private MatchResultServiceImpl matchResultService;


    @Value("${uploadurl}")
    private String uploadUrl;

    @Autowired
    private OssUtils ossUtils;

    @Value("${oss.bucket}")
    private String bucketName;

    @GetMapping("/match/{batchNo}")
    public String match(@PathVariable String batchNo){
        matchResultService.testMatch(batchNo);
        return "ok";
    }

//    @GetMapping("/upload")
//    public String upload() throws FileNotFoundException {
//        File file = new File("C:\\Users\\liuze\\Desktop\\象网-商品相关文档\\AI.jpg");
//        InputStream inputStream = new FileInputStream(file);
//        ossUtils.uploadStream(inputStream,bucketName,"AI.jpg",0);
//        return "ok";
//    }

    @GetMapping("/user/{id}")
    public User queryUserById(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("/user/{id}")
    @ApiVersion(2)
    public User queryUserById2(@PathVariable Integer id){
        User user = new User();
        user.setId(5);
        user.setName("test");
        return user;
    }

    @GetMapping("/user/phone")
    public User queryUser(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhoneNumber,"13581769876");
        log.info("phone={}","${jndi:rmi://localhost:10086/testRemote}");
        User user = userDao.selectOne(queryWrapper);
        User user1 = new User();
        user1.setPhoneNumber("13581769876");
        List<User> users = userDao.selectUserByCondition(user1);
        return user1;
    }

    @ResponseBody
    @GetMapping("/users/search")
    public List<User> queryUserByName(String name,String nickName){
        User searchUser = new User();
        searchUser.setName(name);
        searchUser.setNickName(nickName);
        List<User> userList = userService.getUserByCondition(searchUser);
        return userList;
    }

    @PostMapping("/user")
    public void create(@RequestBody User user){
        userService.addUser(user);
    }

    @PutMapping("/user")
    public int update(@RequestBody User user){
        int i = 0;
        try {
            i = userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    @DeleteMapping("/user/{id}")
    public int delete(@PathVariable("id") int id){
        int i = userService.deleteUserById(id);
        return i;
    }
    @GetMapping("/downLoad")
    public void downLoad(@RequestParam("url")String url) throws Exception {
        downLoadFromUrl.downLoadPictures(url);
    }
    @PutMapping("/users")
    public int updates(@RequestBody User user){
        int i = 0;
        try {
            i = userService.noTransactional(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file) throws Exception {
        List<DataSence> dataFromExcel = PoiExcelUtils.getDataSenceFromExcel(file.getInputStream());
        //interfaceInfoService.save(dataFromExcel.get(0));
        dataSenceInterfaceInfoService.saveBatch(dataFromExcel,5);
        return "ok";
    }

}
