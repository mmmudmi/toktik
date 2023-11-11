package com.scalable.toktik.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void sendMessageToQueue(String queueName, String message) {
        stringRedisTemplate.opsForList().leftPush(queueName, message);
    }

    public String popMessageFromQueue(String queueName) {
        return stringRedisTemplate.opsForList().rightPop(queueName);
    }

    public void publish(String channel, String content) {
        stringRedisTemplate.convertAndSend(channel, content);
    }
}

