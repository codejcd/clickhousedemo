package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(ClickHouseProperties.class)
public class ClickHouseJdbcConfig {

    @Bean(name = "clickhouseDataSource")
    public DataSource clickhouseDataSource(ClickHouseProperties props) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(props.getUrl());
        ds.setUsername(props.getUsername());
        ds.setPassword(props.getPassword());
        ds.setDriverClassName(props.getDriverClassName());
        return ds;
    }

    @Bean(name = "clickhouseJdbcTemplate")
    public JdbcTemplate clickhouseJdbcTemplate() {
        return new JdbcTemplate(clickhouseDataSource(null));
    }

    @Bean(name = "clickhouseNamedJdbcTemplate")
    public NamedParameterJdbcTemplate clickhouseNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(clickhouseDataSource(null));
    }
}
