package com.zhang.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis相关工具
 */
public class JedisUtils {
    private static final JedisPool POOL;
    private  static String host="192.168.25.128";


    static{
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);
        POOL=new JedisPool(config,host,6379);
    }
    public static Jedis getJedis(){
        Jedis jedis= POOL.getResource();
        return jedis;
    }
}