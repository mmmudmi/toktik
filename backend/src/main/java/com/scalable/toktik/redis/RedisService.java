package com.scalable.toktik.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendMessageToQueue(String queueName, String message) {
        redisTemplate.opsForList().leftPush(queueName, message);
    }
}

