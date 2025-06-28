package org.example.coworking.service.config;

import org.example.coworking.config.ReservationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReservationConfig.class)
@ComponentScan(basePackages = {
        "org.example.coworking.service",
})
public class ServiceConfig {
}
