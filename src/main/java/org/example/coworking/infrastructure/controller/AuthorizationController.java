package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.service.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.FILE_LOGGER;

public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Optional<User> authenticate(BufferedReader reader, Class<? extends User> userType) throws IOException {
        boolean isLoggedIn = false;
        Optional<User> possibleUser = Optional.empty();

        while (!isLoggedIn) {
            CONSOLE_LOGGER.info("Enter your name, please.\n");
            String name = reader.readLine().trim();

            CONSOLE_LOGGER.info(name + ", enter your password, please.\n");
            String password = reader.readLine().trim();

            try {
                possibleUser = authorizationService.authenticate(name, password, userType);
            } catch (UserNotFoundException e) {
                CONSOLE_LOGGER.warn(e.getMessage() + "\n" + "Try to log in again. \n");
                FILE_LOGGER.warn(e.getMessage());
                continue;
            }

            if (possibleUser.isPresent()) {
                isLoggedIn = true;
                CONSOLE_LOGGER.info("You have successfully logged in.\n");
            } else {
                FILE_LOGGER.warn("Your login data are wrong. Try again.\n");
                reader.readLine();
            }
        }

        return possibleUser;
    }
}