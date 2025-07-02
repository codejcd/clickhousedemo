package com.example.demo.dto;

import lombok.Data;

@Data
public class AdLogSummary {
    private Long adId;
    private Long impressions;
    private Long clicks;
    private Double cost;
}
