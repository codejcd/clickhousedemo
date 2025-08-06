package com.example.demo.subscriber;

import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private final StatefulRedisPubSubConnection<String, String> pubSubConnection;

    @PostConstruct
    public void subscribe() {
        RedisPubSubListener<String, String> listener = new RedisPubSubListener<>() {
            @Override
            public void message(String channel, String message) {
                log.info("chanel: " + channel + ", msg : " + message);
            }

            @Override public void message(String pattern, String channel, String message) {}
            @Override public void subscribed(String channel, long count) {}
            @Override public void psubscribed(String pattern, long count) {}
            @Override public void unsubscribed(String channel, long count) {}
            @Override public void punsubscribed(String pattern, long count) {}
        };

        pubSubConnection.addListener(listener);
        pubSubConnection.sync().subscribe("chatroom");
    }
}
