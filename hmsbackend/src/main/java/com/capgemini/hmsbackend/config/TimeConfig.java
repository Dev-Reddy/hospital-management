package com.capgemini.hmsbackend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() {
        // Use system default zone; easy to mock in tests
        return Clock.systemDefaultZone();
    }
}
