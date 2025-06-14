package org.example.coworking.service.config;

import org.example.coworking.config.RepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfig.class)
@ComponentScan(basePackages = {
        "org.example.coworking.service",
        "org.example.coworking.service.validator"
})
public class ServiceConfig {
}
