package org.example;

import org.example.coworking.config.AppConfig;
import org.example.coworking.config.DaoConfig;
import org.example.coworking.service.config.ServiceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Entry point of the coworking reservation console application.
 * Initializes the Spring context and starts the main application flow.
 */
public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DaoConfig.class,
                ServiceConfig.class,
                AppConfig.class
        )) {

        }
    }
}

