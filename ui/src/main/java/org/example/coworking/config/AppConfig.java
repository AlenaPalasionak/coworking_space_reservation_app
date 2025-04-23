package org.example.coworking.config;


import org.example.coworking.service.config.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Import(ServiceConfig.class)
@ComponentScan(basePackages = {
        "org.example.coworking.controller",
        "org.example.coworking.mapper"
})
@EnableWebMvc
public class AppConfig {

}
