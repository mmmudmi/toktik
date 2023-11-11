package com.scalable.toktik.controller;

import com.scalable.toktik.redis.RedisService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisService redisService;

    public TestingController(SimpMessagingTemplate simpMessagingTemplate, RedisService redisService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.redisService = redisService;
    }

    @GetMapping("/testing")
    public String testing() {
        simpMessagingTemplate.convertAndSend("/sub/testing", "Hello");
        return "Done";
    }
}
