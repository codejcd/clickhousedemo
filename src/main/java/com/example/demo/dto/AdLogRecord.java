package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdLogRecord {
    private Long adId;
    private LocalDate logDate;
    private Long impressions;
    private Long clicks;
    private Double cost;
}
