package com.scalable.toktik.redis.sub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.util.JsonConverter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketListener implements MessageListener {
    private final StringRedisTemplate stringRedisTemplate;

    public SocketListener(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageContent = new String(message.getBody());
        try {
            SocketStandardRecord record = JsonConverter.decoding(messageContent, SocketStandardRecord.class);
            stringRedisTemplate.convertAndSend(record.endpoint(), record.content());
        } catch (JsonProcessingException ignored) {
        }
//        System.out.println(messageContent);
    }
}
