package com.example.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisStreamProducer {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis Stream에 데이터를 추가하는 메서드
     */
    public Mono<String> pushToStream(String streamKey, Object dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            Map<String, String> payload = Map.of("payload", json);

            return redisTemplate.<String, String>opsForStream()
                    .add(MapRecord.create(streamKey, payload))
                    .map(RecordId::toString);

        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
