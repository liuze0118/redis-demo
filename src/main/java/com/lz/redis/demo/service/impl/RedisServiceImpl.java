package com.lz.redis.demo.service.impl;

import com.lz.redis.demo.service.RedisService;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.vo.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private UserService userService;

    @Cacheable(value = "test",key = "#key",sync = true)
    @Override
    public Good testCache(String key) {
        Good good = new Good();
        good.setName("redis-name");
        System.out.println(key);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if(key.equals("test"))
//            throw new RuntimeException("手动异常");
        return good;
    }
    @Cacheable(value = "test",key = "#method",sync = true)
    @Override
    public String cacheString(String str) {
        System.out.println(str);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str+"test";
    }
    @CacheEvict(value = "test",allEntries = true)
    @Override
    public Good refreshCacheGood(String name) {
        Good good = new Good();
        good.setName(name);
        good.setId(10L);
        good.setBrandName("VIVO");
        return good;
    }
}
