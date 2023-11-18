package com.scalable.toktik.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.comment.CommentRecord;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.util.JsonConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class VideoSocketController {
    private final RedisService redisService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${redis.channel.socket}")
    private String socketChannel;

    public VideoSocketController(RedisService redisService, SimpMessagingTemplate simpMessagingTemplate) {
        this.redisService = redisService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void viewCountSocket(String filename, Integer count) {
        String endpoint = "/sub/views/" + filename;
        try {
            SocketStandardRecord content = new SocketStandardRecord(endpoint, JsonConverter.encoding(count));
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void commentSocket(String filename, CommentRecord record) {
        String endpoint = "/sub/comment/" + filename;
        try {
            SocketStandardRecord content = new SocketStandardRecord(endpoint, JsonConverter.encoding(record));
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void likeCountSocket(String filename, Integer count) {
        String endpoint = "/sub/likes/" + filename;
        try {
            SocketStandardRecord content = new SocketStandardRecord(endpoint, JsonConverter.encoding(count));
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void dislikeCountSocket(String filename, Integer count) {
        String endpoint = "/sub/dislikes/" + filename;
        try {
            SocketStandardRecord content = new SocketStandardRecord(endpoint, JsonConverter.encoding(count));
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void dummy() {
        // Keep this dummy so socket work properly
        simpMessagingTemplate.convertAndSend("/dummy/", "Hello");
    }
}
