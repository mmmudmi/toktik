package com.scalable.toktik.websocket;

import com.scalable.toktik.record.comment.CommentRecord;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class VideoSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public VideoSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void viewCountSocket(String filename, Integer count) {
        simpMessagingTemplate.convertAndSend("/sub/views/" + filename, count);
    }

    public void commentSocket(String filename, CommentRecord record) {
        simpMessagingTemplate.convertAndSend("/sub/comment/" + filename, record);
    }

    public void likeCountSocket(String filename, Integer count) {
        simpMessagingTemplate.convertAndSend("/sub/likes/" + filename, count);
    }

    public void dislikeCountSocket(String filename, Integer count) {
        simpMessagingTemplate.convertAndSend("/sub/dislikes/" + filename, count);
    }


//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//    Thread.sleep(1000); // simulated delay
//    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }
}
