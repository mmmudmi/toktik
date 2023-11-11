package com.scalable.toktik.redis;

import com.scalable.toktik.redis.sub.ErrorListener;
import com.scalable.toktik.redis.sub.FinishListener;
import com.scalable.toktik.redis.sub.SocketListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig {
    @Value("${redis.channel.finish}")
    private String finishChannel;
    @Value("${redis.channel.error}")
    private String errorChannel;
    @Value("${redis.channel.socket}")
    private String socketChannel;

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        // Add some specific configuration such as key serializers, etc.
//        return template;
//    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            @Qualifier("finishListenerAdapter") MessageListenerAdapter finishListener,
            @Qualifier("errorListenerAdapter") MessageListenerAdapter errorListener,
            @Qualifier("socketListenerAdapter") MessageListenerAdapter socketListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(finishListener, new PatternTopic(finishChannel));
        container.addMessageListener(errorListener, new PatternTopic(errorChannel));
        container.addMessageListener(socketListener, new PatternTopic(socketChannel));
        return container;
    }

    @Bean("finishListenerAdapter")
    public MessageListenerAdapter finishListenerAdapter(FinishListener subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean("errorListenerAdapter")
    public MessageListenerAdapter errorListenerAdapter(ErrorListener subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean("socketListenerAdapter")
    public MessageListenerAdapter socketListenerAdapter(SocketListener subscriber) {
        return new MessageListenerAdapter(subscriber);
    }
}
