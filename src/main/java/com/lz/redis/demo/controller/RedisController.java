package com.lz.redis.demo.controller;

import com.lz.redis.demo.service.RedisService;
import com.lz.redis.demo.utils.RedisBloomUtils;
import com.lz.redis.demo.utils.RedisPipelineUtils;
import com.lz.redis.demo.vo.Good;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.util.JedisClusterCRC16;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisPipelineUtils pipelineUtils;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RBloomFilter bloomUtils;
    @GetMapping("/scard")
    public String scard(){
        long[] total = {0};
        Map<JedisPool, List<String>> poolMap = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            String key = "lua-" + i;
            int slot = JedisClusterCRC16.getSlot(key);
            JedisPool pool = pipelineUtils.getSlotPool(slot, "jedisCluster");
            if(poolMap.containsKey(pool)){
                poolMap.get(pool).add(key);
            }else {
                ArrayList<String> keys = new ArrayList<>();
                keys.add(key);
                poolMap.put(pool,keys);
            }
        }
        if(poolMap.size() > 0){
            poolMap.forEach((pool,list)->{
                if(list != null && list.size() > 0){
                    String lua = "return ";
                    for (int i=0;i<list.size();i++) {
                        if(i != 0 )
                            lua = lua + " + redis.call('scard','"+list.get(i)+"')";
                        else
                            lua = lua + "redis.call('scard','"+list.get(i)+"')";
                    }
                    Jedis jedis = pool.getResource();
                    try {
                        total[0] = total[0] + (long)jedis.eval(lua);
                    } finally {
                        if(jedis != null){
                            jedis.close();
                        }
                    }
                }

            });
        }
        return total[0]+"";
    }
    @GetMapping("/scard/pipeline")
    public String scardPipeline(){
        long[] total = {0};
        Map<JedisPool, List<String>> poolMap = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            String key = "lua-" + i;
            int slot = JedisClusterCRC16.getSlot(key);
            JedisPool pool = pipelineUtils.getSlotPool(slot, "jedisCluster");
            if(poolMap.containsKey(pool)){
                poolMap.get(pool).add(key);
            }else {
                ArrayList<String> keys = new ArrayList<>();
                keys.add(key);
                poolMap.put(pool,keys);
            }
        }
        if(poolMap.size() > 0){
            poolMap.forEach((pool,list)->{
                if(list != null && list.size() > 0){
                    Jedis jedis = pool.getResource();
                    Pipeline pipelined = jedis.pipelined();
                    try {
                        List<Response<Long>> responses = list.stream().map(pipelined::scard).collect(Collectors.toList());
                        pipelined.sync();
                        Long count = responses.stream().collect(Collectors.summingLong(Response::get));
                        total[0] = total[0] + count;
                    } finally {
                        if(jedis != null){
                            try {
                                pipelined.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            jedis.close();
                        }
                    }
                }
            });
        }
        return total[0]+"";
    }
    @PostMapping("/get/good")
    @ResponseBody
    public String testPostRequest(){
        Good good = redisService.testCache("test");
        String redis = redisService.cacheString("redis");
        System.out.println(good.getName());
        return good.getName();
    }

    @GetMapping("/refresh/good/{name}")
    @ResponseBody
    public String testPostRequest(@PathVariable String name){
        Good good = redisService.refreshCacheGood(name);
        System.out.println(good.getId());
        return good.getName() ;
    }

    @PutMapping("/bloom/check/{key}")
    @ResponseBody
    public boolean checkBloomKey(@PathVariable String key){
        boolean mayExist = bloomUtils.contains(key);
        return mayExist;
    }
}
