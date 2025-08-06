package com.example.demo.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LettuceConfig {

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379");
    }

    @Bean
    public StatefulRedisPubSubConnection<String, String> pubSubConnection(RedisClient redisClient) {
        return redisClient.connectPubSub();
    }

}
