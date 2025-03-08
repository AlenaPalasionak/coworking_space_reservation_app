package org.example.coworking.infrastructure.controller;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.service.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class AuthorizationController {
    private static final Logger logger = Log.getLogger(AuthorizationController.class);
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Optional<User> authenticate(BufferedReader reader, BufferedWriter writer, Class<? extends User> userType) throws IOException {
        boolean isLoggedIn = false;
        Optional<User> possibleUser = Optional.empty();

        while (!isLoggedIn) {
            writer.write("Enter your name, please.\n");
            writer.flush();
            String name = reader.readLine().trim();

            writer.write(name + ", enter your password, please.\n");
            writer.flush();
            String password = reader.readLine().trim();

            try {
                possibleUser = authorizationService.authenticate(name, password, userType);
            } catch (UserNotFoundException e) {
                logger.warn(e.getMessage());
                writer.write(e.getMessage() + "\n" + "Try to log in again. \n");
                writer.flush();
                continue;
            }

            if (possibleUser.isPresent()) {
                isLoggedIn = true;
                writer.write("You have successfully logged in.\n");
                writer.flush();
            } else {
                writer.write("Your login data are wrong. Try again.\n");
                writer.flush();
                reader.readLine();
            }
        }

        return possibleUser;
    }
}