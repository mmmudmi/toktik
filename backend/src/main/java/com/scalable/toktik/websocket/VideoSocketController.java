package com.scalable.toktik.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.comment.CommentRecord;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.util.JsonConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class VideoSocketController {
    private final RedisService redisService;
    @Value("${redis.channel.socket}")
    private String socketChannel;

    public VideoSocketController(RedisService redisService) {
        this.redisService = redisService;
    }

    public void viewCountSocket(String filename, Integer count) {
        String endpoint = "/sub/views/" + filename;
        SocketStandardRecord content = new SocketStandardRecord(endpoint, count);
        try {
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }


    public void commentSocket(String filename, CommentRecord record) {
        String endpoint = "/sub/comment/" + filename + filename;
        SocketStandardRecord content = new SocketStandardRecord(endpoint, record);
        try {
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void likeCountSocket(String filename, Integer count) {
        String endpoint = "/sub/likes/" + filename;
        SocketStandardRecord content = new SocketStandardRecord(endpoint, count);
        try {
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }

    public void dislikeCountSocket(String filename, Integer count) {
        String endpoint = "/sub/dislikes/" + filename;
        SocketStandardRecord content = new SocketStandardRecord(endpoint, count);
        try {
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }


//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//    Thread.sleep(1000); // simulated delay
//    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }
}
