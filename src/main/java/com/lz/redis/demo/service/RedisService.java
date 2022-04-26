package com.lz.redis.demo.service;

import com.lz.redis.demo.vo.Good;

public interface RedisService {
    Good testCache(String key);
    String cacheString(String str);
    Good refreshCacheGood(String name);
}
