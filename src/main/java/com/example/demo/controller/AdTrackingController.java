package com.example.demo.controller;

import com.example.demo.dto.AdTrackingEvent;
import com.example.demo.producer.RedisStreamProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad-tracking")
public class AdTrackingController {

    private final RedisStreamProducer streamProducer;

    @PostMapping
    public Mono<ResponseEntity<String>> track(@RequestBody Mono<AdTrackingEvent> eventMono) {
        return eventMono.flatMap(e -> streamProducer.pushToStream("ad_tracking:stream", e))
                .map(id -> ResponseEntity.ok("queued: " + id));
    }

}
