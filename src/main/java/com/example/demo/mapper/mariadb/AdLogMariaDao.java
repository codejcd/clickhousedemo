package com.example.demo.mapper.mariadb;

import com.example.demo.dto.AdLogSummary;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
@RequiredArgsConstructor
public class AdLogMariaDao {

    private final SqlSession sqlSession;

    public List<AdLogSummary> getSummary(LocalDate from, LocalDate to) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);

        return sqlSession.selectList("AdLogMariaMapper.getSummary", params);
    }
}