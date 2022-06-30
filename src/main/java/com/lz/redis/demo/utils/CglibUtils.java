package com.lz.redis.demo.utils;

import com.lz.redis.demo.service.impl.UserServiceImpl;

public class CglibUtils {

    public static void main(String[] args) {
        UserServiceImplCglib cglib = new UserServiceImplCglib();
        UserServiceImpl instance = cglib.getInstance(new UserServiceImpl(), UserServiceImpl.class);
        instance.spurMethod();
        instance.subMethod();
    }

}
