package com.scalable.toktik.redis;

import com.scalable.toktik.redis.sub.ErrorListener;
import com.scalable.toktik.redis.sub.FinishListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    private final String finishChannel = "finish_channel";
    private final String errorChannel = "error_channel";

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            @Qualifier("finishListenerAdapter") MessageListenerAdapter finishListener,
            @Qualifier("errorListenerAdapter") MessageListenerAdapter errorListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(finishListener, new PatternTopic(finishChannel));
        container.addMessageListener(errorListener, new PatternTopic(errorChannel));
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
}
