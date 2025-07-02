package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MariaDBMybatisConfig {
        @Bean(name = "mariadbDataSource")
        @Primary
        public DataSource mariadbDataSource() {
                HikariDataSource ds = new HikariDataSource();
                ds.setJdbcUrl("jdbc:mariadb://localhost:3306/default");
                ds.setUsername("root");
                ds.setPassword("1234");
                ds.setDriverClassName("org.mariadb.jdbc.Driver");
                return ds;
        }

        @Bean(name = "mariadbSqlSessionFactory")
        @Primary
        public SqlSessionFactory mariadbSqlSessionFactory(
                @Qualifier("mariadbDataSource") DataSource dataSource) throws Exception {
                SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
                factory.setDataSource(dataSource);
                factory.setMapperLocations(
                        new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
                factory.setTypeAliasesPackage("com.example.demo.dto");
                return factory.getObject();
        }

        @Bean(name = "sqlSession")
        @Primary
        public SqlSession mariadbSqlSession(
                @Qualifier("mariadbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
                return new SqlSessionTemplate(sqlSessionFactory);
        }
}
