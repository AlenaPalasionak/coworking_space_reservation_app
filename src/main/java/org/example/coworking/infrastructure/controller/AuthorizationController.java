package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.service.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Optional<User> authenticate(BufferedReader reader, Class<? extends User> userType) throws IOException {
        while (true) {
            USER_OUTPUT_LOGGER.info("Enter your name, please.");
            String name = reader.readLine().trim();

            USER_OUTPUT_LOGGER.info(name + ", enter your password, please.");
            String password = reader.readLine().trim();

            try {
                Optional<User> possibleUser = authorizationService.authenticate(name, password, userType);

                if (possibleUser.isPresent()) {
                    USER_OUTPUT_LOGGER.info("You have successfully logged in.");
                    return possibleUser;
                } else {
                    USER_OUTPUT_LOGGER.warn("Your login data are incorrect. Try again.");
                }
            } catch (UserNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + "\nTry to log in again.");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }
}