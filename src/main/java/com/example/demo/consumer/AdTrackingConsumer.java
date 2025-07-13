package com.example.demo.consumer;

import com.example.demo.dto.AdTrackingEvent;
import com.example.demo.service.AdTrackingInserter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdTrackingConsumer {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AdTrackingInserter adTrackingInserter;

    private static final String STREAM_KEY = "ad_tracking:stream";
    private static final String GROUP = "ad-tracking-group";
    private static final String CONSUMER = "consumer-1";

    @PostConstruct
    public void startConsumer() {
        createGroupIfNeeded();
        consumeLoop();
    }

    private void createGroupIfNeeded() {
        redisTemplate.opsForStream()
                .createGroup(STREAM_KEY, ReadOffset.latest(), GROUP)
                .doOnError(e -> log.info("Group already exists or create failed: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    private void consumeLoop() {
        Flux.interval(Duration.ofSeconds(2))
                .flatMap(tick ->
                        redisTemplate.<String, String>opsForStream()
                                .read(Consumer.from(GROUP, CONSUMER),
                                        StreamReadOptions.empty().count(100),
                                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()))
                )
                .flatMap(this::handleRecord)
                .subscribe();
    }

    private Flux<Void> handleRecord(MapRecord<String, String, String> record) {
        String payload = record.getValue().get("payload");

        try {
            AdTrackingEvent event = objectMapper.readValue(payload, AdTrackingEvent.class);

            return adTrackingInserter.insertBatch(java.util.List.of(event))
                    .then(redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, record.getId()))
                    .thenMany(Flux.empty());

        } catch (Exception e) {
            log.error("Invalid payload or parse error: {}", e.getMessage());
            return Flux.empty();
        }
    }
}
