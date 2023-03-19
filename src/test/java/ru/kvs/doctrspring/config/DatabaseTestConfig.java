package ru.kvs.doctrspring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Slf4j
@TestConfiguration
public class DatabaseTestConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static PostgreSQLContainer postgres;

    static {
        postgres = new PostgreSQLContainer("postgres:13")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");

        postgres.start();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgres.getDriverClassName())
                .url(postgres.getJdbcUrl())
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .build();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();
    }
}
