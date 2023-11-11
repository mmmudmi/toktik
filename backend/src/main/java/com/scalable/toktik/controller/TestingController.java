package com.scalable.toktik.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestingController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/testing")
    public String testing() {
        simpMessagingTemplate.convertAndSend("/sub/testing", "Hello");
        return "Done";
    }
}
