package com.lz.redis.demo.contorller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.redis.demo.annotatiion.ApiVersion;
import com.lz.redis.demo.dao.UserDao;
import com.lz.redis.demo.download.DownLoadFromUrl;
import com.lz.redis.demo.model.entity.mysql.HealthInterfaceInfo;
import com.lz.redis.demo.service.HealthInterfaceInfoService;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.utils.PoiExcelUtils;
import com.lz.redis.demo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @Value("${java.home}")
    private String javahome;

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
        List<HealthInterfaceInfo> dataFromExcel = PoiExcelUtils.getDataFromExcel(file.getInputStream());
        //interfaceInfoService.save(dataFromExcel.get(0));
        interfaceInfoService.saveBatch(dataFromExcel,5);
        return "ok";
    }

}
