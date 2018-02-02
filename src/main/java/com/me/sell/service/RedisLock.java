package com.me.sell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 由于redis是单线程的，因此可以把要加锁的变量放到redis里操作
     * @param key  要加锁的变量
     * @param value  锁超时时间  （当前时间+超时时间）
     * @return true可以加锁，false无法再加锁
     */
    public boolean lock(String key, String value){
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        //判断加锁时间，防止死锁
        //假设之前已经有一个线程把expire设置为A
        String expire = redisTemplate.opsForValue().get(key).toString();
        if(!StringUtils.isEmpty(expire) &&
                Long.parseLong(expire) < System.currentTimeMillis()){//锁过期了
            //两个线程同时进到了这里，带着value B
            String oldExpire = redisTemplate.opsForValue().getAndSet(key,value).toString();//redis操作是单线程的，only one thread execute this at a time
            //所以只能有一个线程拿到了expire的A，其他线程只能拿到value B
            //而拿到A的则可以拿到锁
            if(!StringUtils.isEmpty(oldExpire) &&
                    oldExpire.equals(expire)){
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁，就是删除加锁线程设置进去的key value
     * @param key
     * @param value
     */
    public void unlock(String key, String value){
        try{
            String expire = redisTemplate.opsForValue().get(key).toString();
            if(!StringUtils.isEmpty(expire) && expire.equals(value)){//是同一个线程加的锁
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
