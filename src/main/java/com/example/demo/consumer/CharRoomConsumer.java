package com.example.demo.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CharRoomConsumer {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private static final String STREAM_KEY = "chatroom-stream";

    public Flux<String> readAllMessages() {
        return redisTemplate.opsForStream()
                .range(STREAM_KEY, Range.unbounded())
                .map(record -> {
                    Map<Object, Object> map = record.getValue();
                    return map.get("sender") + ": " + map.get("content");
                });
    }

}
