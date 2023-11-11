package com.scalable.toktik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.notification.NotificationRecordTool;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.service.NotificationService;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.util.JsonConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisService redisService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationRecordTool notificationRecordTool;

    public TestingController(SimpMessagingTemplate simpMessagingTemplate, RedisService redisService, NotificationService notificationService, UserService userService, NotificationRecordTool notificationRecordTool) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.redisService = redisService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationRecordTool = notificationRecordTool;
    }

    @GetMapping("/testing")
    public String testing() {
        simpMessagingTemplate.convertAndSend("/sub/testing", "Hello");
        return "Done";
    }

    @GetMapping("/socket")
    public void socket_testing() throws JsonProcessingException {
        String endpoint = "/sub/views/";
        SocketStandardRecord record = new SocketStandardRecord(endpoint, 1);
        try {
            redisService.publish("socket_channel", JsonConverter.encoding(record));
        } catch (JsonProcessingException ignored) {
        }
    }
}
