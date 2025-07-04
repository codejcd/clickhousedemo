package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "clickhouse.datasource")
public class ClickHouseProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}