package org.example.coworking.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.example.coworking.config",
        "org.example.coworking.repository",
})
public class ReservationConfig {
}
