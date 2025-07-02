package com.example.demo.controller;

import com.example.demo.dto.AdLogSummary;
import com.example.demo.mapper.mariadb.AdLogMariaDao;
import com.example.demo.repository.ClickHouseAdLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final JdbcTemplate jdbcTemplate;

    private final AdLogMariaDao mariaMapper;
    private final ClickHouseAdLogRepository clickhouseRepo;

    @GetMapping("/logs")
    public List<Map<String, Object>> getLogs() {
        return jdbcTemplate.queryForList("SELECT * FROM test_log LIMIT 10");
    }

    @GetMapping("/daily-summary")
    public List<Map<String, Object>> getDailySummary(
            @RequestParam(defaultValue = "7") int days) {

        String sql = """
            SELECT
                date,
                campaign_id,
                impressions,
                clicks,
                ctr,
                conversions,
                cvr,
                avg_cpc,
                total_cost,
                roas
            FROM (
                SELECT
                    date,
                    campaign_id,
                    impressions,
                    clicks,
                    round(clicks / nullIf(impressions,0) * 100,2) AS ctr,
                    conversions,
                    round(conversions / nullIf(clicks,0) * 100,2) AS cvr,
                    round(cost / nullIf(clicks,0),2) AS avg_cpc,
                    cost AS total_cost,
                    round(revenue / nullIf(cost,0) * 100,2) AS roas
                FROM ad_campaign_daily_summary
                WHERE date >= today() - ?
            )
            ORDER BY date DESC, campaign_id
        """;

        return jdbcTemplate.queryForList(sql, days);
    }

    @GetMapping("/mariadb")
    public List<AdLogSummary> getMaria(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return mariaMapper.getSummary(from, to);
    }

    @GetMapping("/clickhouse")
    public List<AdLogSummary> getClickHouse(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return clickhouseRepo.getSummary(from, to);
    }

}
