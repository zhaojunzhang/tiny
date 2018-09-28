package com.tiny.common.util;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author  by zhangzhaojun on 2017/9/12.
 */
@Component
@DependsOn("redisTemplate")
public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redis;

    private  static RedisTemplate redisTemplate;


    @PostConstruct
    public void init(){
        redisTemplate = this.redis;
    }


    public static final String PREFIX = "TEACHER:GROWTH:SERVICE:%s";




    public static Object get(String key){
        Object result = null;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            result = operations.get(key);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }

        return result;
    }


    public static boolean set(String key, Object value, long timeout ) {
        boolean result = false;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(key, value, timeout, TimeUnit.SECONDS);
            result = true;
        }catch (Exception e){
            logger.info(String.format("key:%s;value:%s",key,value),e);
        }
        return result;
    }


    /**
     * zset 入值
     * @param key
     * @param
     * @param timeout
     * @return
     */
    public static boolean zSetAdd(String key, Set<ZSetOperations.TypedTuple<Object>> value, long timeout ) {
        boolean result = false;
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.add(key,value);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);

            result = true;
        }catch (Exception e){
            logger.info(String.format("key:%s;value:%s",key,value),e);
        }
        return result;
    }


    /**
     * zSet  根据key移除某个元素
     * @param key
     * @param value
     * @return
     */
    public static Long zSetRemove(String key,Object value) {
        Long result = 0L;
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            result = zSetOperations.remove(key,value);
        }catch (Exception e){
            logger.info(String.format("key:%s;value:%s",key,value),e);
        }
        return result;
    }


    public static Set<Object> zSetRang(String key,Long start,Long end) {
        Set result = null;
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            result = zSetOperations.range(key, start, end);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return result;
    }

    public static Long zSetSize(String key) {
        Long result = 0L;
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            result = zSetOperations.size(key);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return result;
    }


    public static boolean zSetAdd(String key,Object value, double var3 , Long timeout) {
        boolean result = false;
        try {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            result = zSetOperations.add(key,value,var3);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return result;
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    public static String getKey(String prefix,List<Object> arge){
        if(CollectionUtils.isEmpty(arge)){
            return String.format(PREFIX,prefix);
        }
        return String.format(PREFIX,prefix+":"+ StringUtils.join(arge,"_"));
    }


    public static String getKey(String prefix,Object... arge){
        if(ArrayUtils.isEmpty(arge)){
            return String.format(PREFIX,prefix);
        }
        return String.format(PREFIX,prefix+":"+ StringUtils.join(arge,"_"));
    }


    /**
     * 批量get
     * @param keys
     * @param <T>
     * @return
     */
    public  static <T>  List<T> multiGet(List<String> keys,Class<T> clazz) {
        if(CollectionUtils.isEmpty(keys)) {
            return Lists.newArrayList();
        }
        try {
            List<T> valueList = redisTemplate.opsForValue().multiGet(keys);
            if(CollectionUtils.isEmpty(valueList)){
                return Lists.newArrayList();
            }
            List<T> result = Lists.newArrayList();
            for(T t:valueList){
                if(Objects.nonNull(t)){
                    result.add(t);
                }
            }

            return result;
        }catch (Exception e){
            logger.info(String.format("key:%s",JsonUtils.toJson(keys)),e);
            return Lists.newArrayList();
        }
    }



    public static <K,V> boolean multiSet(Map<K,V> map,long second){
        if(CollectionUtils.isEmpty(map)){
            return true;
        }

        try{

            List<K> resultList = Lists.newArrayList();
            redisTemplate.opsForValue().multiSet(map);
            map.keySet().forEach(key -> {
                if(redisTemplate.expire(key, second, TimeUnit.SECONDS)){
                    resultList.add(key);
                }
            });

            return resultList.size() == map.size();

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
    }


    public static <V> boolean lpush(String key,Collection<V> values,long second) {
        boolean result = false;
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            listOperations.leftPushAll(key,values);
            redisTemplate.expire(key, second, TimeUnit.SECONDS);
            result=true;
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return result;
    }

    public static <V> Collection<V> lrange(String key,Class<V> clazz){
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            return listOperations.range(key,0L,-1L);

        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return null;
    }

    public static void lremove(String key){
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            listOperations.trim(key,1L,0L);

        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }

    }

    /**
     * 分布式锁
     * @param key
     * @param expire 过期时间 秒
     * @return
     */
    public static boolean lock(String key, long expire) {
        boolean ret = false;
        try {
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            ret = connection.setNX(key.getBytes(), "1".getBytes());
            if (ret) {
                connection.expire(key.getBytes(), expire);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 删除 list key 中 第一个 value
     * @param key
     * @param value
     * @return
     */
    public static Long lrem(String key, Object value) {
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            return listOperations.remove(key, 1, value);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return 0L;
    }

    /**
     * list 长度
     * @param key
     * @return
     */
    public static Long llen(String key) {
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            return listOperations.size(key);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return 0L;
    }

    public static <V> Long lpush(String key, V values, long second) {
        Long ret = 0L;
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            ret = listOperations.leftPush(key, values);
            redisTemplate.expire(key, second, TimeUnit.SECONDS);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return ret;
    }

    public static <V> V rpop(String key) {
        try {
            ListOperations listOperations = redisTemplate.opsForList();
            return (V) listOperations.rightPop(key);
        }catch (Exception e){
            logger.info(String.format("key:%s",key),e);
        }
        return null;
    }
}
