package com.example.demo.controller;

import com.example.demo.consumer.CharRoomConsumer;
import com.example.demo.producer.ChatRoomStreamProducer;
import com.example.demo.publisher.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisMessagePublisher publisher;

    private final ChatRoomStreamProducer producerService;
    private final CharRoomConsumer consumerService;

    /**
     * Redis pub/sub
     * @param message
     * @return
     */
    @PostMapping("/pubsub/send")
    public String send(@RequestParam String message) {
        publisher.publish("chatroom", message);
        return "메시지 전송 완료";
    }

    /**
     * Redis Stream Sender
     * @param sender
     * @param content
     * @return
     */
    @PostMapping("/send")
    public Mono<String> send(@RequestParam String sender, @RequestParam String content) {
        return producerService.sendMessage(sender, content)
                .map(id -> "메시지 저장 완료 (ID: " + id + ")");
    }

    /**
     * Redis Stream Reader
     * @return
     */
    @GetMapping(value = "/read", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> readAll() {
        return consumerService.readAllMessages();
    }
}
