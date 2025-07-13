package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdTrackingEvent {
    private LocalDateTime eventTime;
    private String adId;
    private String campaignId;
    private Long impressions;
    private Long clicks;
    private Double cost;
    private Long conversions;
    private Double revenue;
}
