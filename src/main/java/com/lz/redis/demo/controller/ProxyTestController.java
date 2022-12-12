package com.lz.redis.demo.controller;

import com.lz.redis.demo.annotatiion.ApiVersion;
import com.lz.redis.demo.annotatiion.SysLog;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : liuze
 * @date: 2022/10/13 14:50
 **/
@RestController
@RequestMapping("/proxy/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProxyTestController {
    private final UserService userService;
//    @Autowired
//    private UserService userService;

    @GetMapping("{version}/user/{id}")
    public User queryUserById(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("{version}/user/{id}")
    @ApiVersion(2)
    User queryUserById2(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("{version}/user/{id}")
    @ApiVersion(3)
    @SysLog
    private User queryUserById3(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }
}
