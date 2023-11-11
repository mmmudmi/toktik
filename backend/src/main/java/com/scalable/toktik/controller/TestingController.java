package com.scalable.toktik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.util.JsonConverter;
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

    @GetMapping("/socket")
    public void socket_testing() {
        String endpoint = "/sub/views/";
        SocketStandardRecord record = new SocketStandardRecord(endpoint, "1");
        try {
            redisService.publish("socket_channel", JsonConverter.encoding(record));
        } catch (JsonProcessingException ignored) {
        }
    }
}
