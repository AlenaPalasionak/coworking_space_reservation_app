package org.example;

import org.example.coworking.config.AppConfig;
import org.example.coworking.config.DaoConfig;
import org.example.coworking.controller.FlowController;
import org.example.coworking.service.config.ServiceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Entry point of the coworking reservation console application.
 * Initializes the Spring context and starts the main application flow.
 */
public class Main {

    /**
     * Launches the application by creating the Spring context and delegating control
     * to {@link FlowController}. Ensures proper resource cleanup on exit.
     */
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DaoConfig.class,
                ServiceConfig.class,
                AppConfig.class
        )) {
            FlowController flowController = context.getBean(FlowController.class);
            flowController.startAppFlow();
        }
    }
}

