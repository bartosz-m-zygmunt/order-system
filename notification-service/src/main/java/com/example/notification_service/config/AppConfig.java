package com.example.notification_service.config;

import org.springframework.context.annotation.Bean;

public class AppConfig {

    @Bean
    public String appName() {
        return "Notification Service";
    }
}
