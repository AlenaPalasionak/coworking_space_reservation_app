package org.example.coworking.service.config;

import org.example.coworking.config.DaoConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DaoConfig.class)
@ComponentScan(basePackages = {
        "org.example.coworking.service",
        "org.example.coworking.service.validator"
})
public class ServiceConfig {
}
