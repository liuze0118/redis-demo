package com.lz.redis.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuze
 * @date: 2022/10/28 16:42
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.addNodeAddress("redis://47.94.237.238:7001","redis://47.94.237.238:7002","redis://47.94.237.238:7003","redis://47.94.237.238:7004","redis://47.94.237.238:7005","redis://47.94.237.238:7006");
        clusterServersConfig.setPassword("liuze0118");
        return Redisson.create(config);
    }

    @Bean
    public RBloomFilter initBloomFilter(RedissonClient redissonClient){
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("name", new StringCodec());
        bloomFilter.tryInit(10L,0.01);
        for (int i = 0; i < 10; i++) {
            bloomFilter.add("name"+i);
        }
        return bloomFilter;
    }

}
