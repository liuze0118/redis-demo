package com.lz.redis.demo.service.impl;

import com.xxl.job.core.handler.annotation.XxlJob;

public class BaseServiceImpl<T> {
    @XxlJob(value = "")
    public void spurMethod(){
        System.out.println("----------i m  s p u r ---------");
    }
}
