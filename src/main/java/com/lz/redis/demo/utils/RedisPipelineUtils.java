package com.lz.redis.demo.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RedisPipelineUtils {


    private Map<String,JedisCluster> clusterMap;

    private Map<String,ClusterNode> nodeMap = new HashMap<>();


    public RedisPipelineUtils(Map<String, JedisCluster> map) {
        this.clusterMap = map;
        if(!clusterMap.isEmpty()){
            clusterMap.forEach((key,cache)->{
                nodeMap.put(key,new ClusterNode(cache));
            });
        }
    }

    @Data
    public static class ClusterNode{
        private JedisSlotBasedConnectionHandler connectionHandler;

        private JedisClusterInfoCache cache;

        public ClusterNode(JedisCluster cacheWrapper) {
            try {
                JedisCluster jedis = cacheWrapper;
                Class<? extends JedisCluster> clazz = jedis.getClass();
                Class<?> superClazz = clazz.getSuperclass();
                Field field = superClazz.getDeclaredField("connectionHandler");
                field.setAccessible(true);
                connectionHandler= (JedisSlotBasedConnectionHandler)field.get(jedis);
                Class<? extends JedisSlotBasedConnectionHandler> clazz1 = connectionHandler.getClass();
                Class<?> superclazz1 = clazz1.getSuperclass();
                Field fieldCache = superclazz1.getDeclaredField("cache");
                fieldCache.setAccessible(true);
                cache = (JedisClusterInfoCache)fieldCache.get(connectionHandler);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                log.info("初始化redisPipeline工具类失败");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public JedisPool getSlotPool(int slot,String clusterKey){
        ClusterNode clusterNode = nodeMap.get(clusterKey);
        JedisClusterInfoCache cache = clusterNode.getCache();
        JedisSlotBasedConnectionHandler connectionHandler = clusterNode.getConnectionHandler();
        JedisPool jedisPool = cache.getSlotPool(slot);
        if (jedisPool != null) {
            return jedisPool;
        } else {
            connectionHandler.renewSlotCache(); //It's abnormal situation for cluster mode, that we have just nothing for slot, try to rediscover state
            jedisPool = cache.getSlotPool(slot);
            if (jedisPool != null) {
                return jedisPool;
            } else {
                log.error("未获取到slot对应的pool");
                throw new RuntimeException("No reachable node in cluster for slot " + slot);
            }
        }
    }
}
