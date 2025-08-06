package com.example.demo.publisher;

import com.example.demo.dto.RedisMessage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisMessagePublisher {

    private final RedisClient redisClient;

    public void publish(String channel, String message) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisCommands<String, String> sync = connection.sync();
            sync.publish(channel, message);
        }
    }
}
