package com.lz.redis.demo;

import com.lz.redis.demo.service.RedisService;
import com.lz.redis.demo.vo.Good;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class RedisDemoApplicationTests {
    @Autowired
    private RedisService redisService;
    @Test
    void contextLoads() {
    }

    @Test
    public void cacheThreadTest(){
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Good good = redisService.testCache("test");
                System.out.println(good.getName());
            }).start();
            countDownLatch.countDown();
        }
    }
}
