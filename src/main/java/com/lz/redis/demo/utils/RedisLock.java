package com.lz.redis.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RedisLock {

    private  RedisTemplate redisTemplate;

    private ThreadLocal<String> localValue;

    private  int expireTime = 10;

    private final int sleepTime = -1;
    //todo 使用ThreadLocal保证智能打断当前线程
    private ThreadLocal<Boolean>  interrupted = ThreadLocal.withInitial(() -> true);

    private boolean activating = false;

    private int timeOut = -1;

    public RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void lock(String lockName) throws TimeoutException, InterruptedException {
        this.lock(lockName,this.expireTime,activating,this.sleepTime,this.timeOut);
    }

    public void lock(String lockName,int expireTime) throws InterruptedException, TimeoutException {
        this.lock(lockName,expireTime,this.activating,this.sleepTime,this.timeOut);
    }
    public void lock(String lockName,int expireTime,int timeOut) throws TimeoutException, InterruptedException {
        this.lock(lockName,expireTime,this.activating,this.sleepTime,timeOut);
    }
    public void lock(String lockName,int expireTime,boolean activating) throws TimeoutException, InterruptedException {
        this.lock(lockName,expireTime,activating,this.sleepTime,this.timeOut);
    }
    public void lock(String lockName,int expireTime,boolean activating,int sleepTime,int timeOut) throws InterruptedException, TimeoutException {
        boolean result = tryLock(lockName, expireTime,activating);
        if(result)
            return;
        long outed = System.currentTimeMillis() + (timeOut*1000);
        while (!result) {
            if(timeOut > 0 && System.currentTimeMillis() > outed){
                throw new TimeoutException("获取redis锁超时");            }
            if (!interrupted.get()) {
                throw new InterruptedException("获取锁状态被中断");
            }
            result = tryLock(lockName,expireTime);
            if (!result && sleepTime >0) {
                try {
                    Thread.sleep(sleepTime*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void lockInterrupted() throws InterruptedException {
        this.interrupted.set(false);
    }

    public boolean tryLock(String lockName,int expireTime) {
        return this.tryLock(lockName,expireTime,this.activating);
    }

    public boolean tryLock(String lockName) {
        return this.tryLock(lockName,this.expireTime,this.activating);
    }


    public boolean tryLock(String lockName,int expireTime,boolean activating) {
        if (lockName == null) {
            throw new NullPointerException("lockName is null");
        }
        String uuid = UUID.randomUUID().toString();
        int expire = expireTime>0?expireTime:this.expireTime;
        boolean execute =(Boolean)redisTemplate.execute((RedisCallback) redisConnection -> {
            Object obj = redisConnection.execute("set", redisTemplate.getStringSerializer().serialize(lockName),
            redisTemplate.getStringSerializer().serialize(uuid), SafeEncoder.encode("NX"), SafeEncoder.encode("EX"), Protocol.toByteArray(expire));
            return obj != null;
        });
        if (execute) {
            //todo 保存当前线程所持有的锁的value,防止其他线程误删，强删
            localValue = new ThreadLocal<>();
            localValue.set(uuid);
            log.info("获取Redis锁成功,key={},value={}", lockName, uuid);
            //todo 此时可以开启线程轮询为当前锁续活
            if (activating) {
                //过期时间小于10秒不支持续活
                if (expireTime < 10) {
                    return true;
                }
                int ex = expire;
                String value = localValue.get();
                Thread actThread = new Thread(() -> {
                    long activatingTime = (ex - 9) * 1000;
                    while (true) {
                        if (!this.interrupted.get()) {
                            throw new RuntimeException("续活锁操作被打断,key=" + lockName);
                        }
                        try {
                            Thread.sleep(activatingTime);
                            //todo lua脚本保证操作原子性(判断redis里是否持有当前线程的锁,true则续活，false则结束续活线程
                            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1],ARGV[2]) else return 0 end";
                            List<String> keys = new ArrayList();
                            keys.add(lockName);
                            Object actResult = redisTemplate.execute(new DefaultRedisScript(script,Long.class),keys,value,ex+"");
                            //Object actResult = cacheProvider.eval(script, lockName, value, ex + "");
                            if ("1".equalsIgnoreCase(actResult + "")) {
                                log.info("redis锁续活" + ex + "s成功,key={}", lockName);
                            } else {
                                log.info("redis锁续活,key={},value={}", lockName, value);
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //todo 续活线程置为守护线程,主线程停止时,续活线程即停止
                actThread.setDaemon(Boolean.TRUE);
                actThread.start();
            }
            return true;
        }else {
            return false;
        }
    }

    public void unlock(String lockName) {
        try {
            if (StringUtils.isEmpty(lockName)) {
                throw new RuntimeException("释放redis锁时,锁名称不能为空");
            }
            if(localValue == null || StringUtils.isEmpty(localValue.get())){
               log.info("当前线程未持有锁 key={}",lockName);
            }else{
                //todo lua保证操作原子性,判断redis持有的是否当前线程的锁,true则删除,false则跳过
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                List<String> keys = new ArrayList();
                keys.add(lockName);
                Object result = redisTemplate.execute(new DefaultRedisScript(script,Long.class),keys,localValue.get());
                if("1".equalsIgnoreCase(result+"")){
                    log.info("redis锁释放成功,key={},value={}",lockName,localValue.get());
                }else{
                    log.info("redis锁释放失败,key={},value={}",lockName,localValue.get());
                }
                localValue.remove();
            }

        } catch (Exception e) {
            log.error("method=unlock,desc=删除锁异常,lockName={},emsg={}", lockName, e);
        }
    }

}
