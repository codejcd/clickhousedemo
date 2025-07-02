package com.example.demo.repository;

import com.example.demo.dto.AdLogSummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class ClickHouseAdLogRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public ClickHouseAdLogRepository(@Qualifier("clickhouseNamedJdbcTemplate") NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<AdLogSummary> getSummary(LocalDate from, LocalDate to) {
        String sql = """
            SELECT ad_id,
                   SUM(impressions) AS impressions,
                   SUM(clicks) AS clicks,
                   SUM(cost) AS cost
            FROM ad_log
            WHERE log_date BETWEEN :from AND :to
            GROUP BY ad_id
        """;

        Map<String, Object> params = Map.of("from", from, "to", to);

        return jdbc.query(sql, params, (rs, rowNum) -> {
            AdLogSummary dto = new AdLogSummary();
            dto.setAdId(rs.getLong("ad_id"));
            dto.setImpressions(rs.getLong("impressions"));
            dto.setClicks(rs.getLong("clicks"));
            dto.setCost(rs.getDouble("cost"));
            return dto;
        });
    }
}
