package com.scalable.toktik.redis.sub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.util.JsonConverter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
@Controller
public class SocketListener implements MessageListener {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SocketListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageContent = new String(message.getBody());
        System.out.println(messageContent);
        try {
            SocketStandardRecord record = JsonConverter.decoding(messageContent, SocketStandardRecord.class);
            simpMessagingTemplate.convertAndSend(record.endpoint(), record.content());
        } catch (JsonProcessingException ignored) {
        }
    }
}
