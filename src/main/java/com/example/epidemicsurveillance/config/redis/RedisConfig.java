package com.example.epidemicsurveillance.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * @ClassName RedisConfig
 * @Author 朱云飞
 * @Date 2021/6/15 16:58
 * @Version 1.0
 **/
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        //String类型Key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //String类型Value序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //Hash类型的key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash类型的Value序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}