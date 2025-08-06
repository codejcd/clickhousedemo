package com.example.demo.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomStreamProducer {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private static final String STREAM_KEY = "chatroom-stream";

    public Mono<String> sendMessage(String sender, String content) {
        Map<String, String> message = Map.of(
                "sender", sender,
                "content", content
        );

        return redisTemplate.opsForStream()
                .add(STREAM_KEY, message)
                .map(recordId -> {
                    System.out.println("저장된 메시지 ID: " + recordId.getValue());
                    return recordId.getValue();
                });
    }
}
