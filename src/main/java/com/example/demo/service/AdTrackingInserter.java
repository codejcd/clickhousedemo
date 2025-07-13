package com.example.demo.service;

import com.example.demo.dto.AdTrackingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdTrackingInserter {

    private final WebClient clickhouseWebClient;

    public Mono<Void> insertBatch(List<AdTrackingEvent> records) {
        if (records.isEmpty()) return Mono.empty();

        StringBuilder sb = new StringBuilder(
                "INSERT INTO ad_tracking (event_time, ad_id, campaign_id, impressions, clicks, cost, conversions, revenue) FORMAT TSV\n");

        for (AdTrackingEvent r : records) {
            sb.append(String.format("%s\t%s\t%s\t%d\t%d\t%f\t%d\t%f\n",
                    r.getEventTime().toString().replace("T", " "),
                    r.getAdId(),
                    r.getCampaignId(),
                    r.getImpressions(),
                    r.getClicks(),
                    r.getCost(),
                    r.getConversions(),
                    r.getRevenue()));
        }

        return clickhouseWebClient.post()
                .uri("/?query=INSERT INTO ad_tracking (event_time, ad_id, campaign_id, impressions, clicks, cost, conversions, revenue) FORMAT TSV")
                .bodyValue(sb.toString())
                .retrieve()
                .bodyToMono(String.class)
                .then();
    }
}
