package ru.kvs.doctrspring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    private final static LocalDate TEST_LOCAL_DATE = LocalDate.of(2022, 9, 15);

    @Bean
    @ConditionalOnMissingBean
    Clock getSystemDefaultZoneClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    @Profile("integration-test")
    @Primary
    Clock getFixedClock() {
        return Clock.fixed(TEST_LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

}
