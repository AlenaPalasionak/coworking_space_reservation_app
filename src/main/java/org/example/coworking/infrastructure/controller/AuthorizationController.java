package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.controller.exception.InvalidInputException;
import org.example.coworking.infrastructure.controller.validator.InputValidator;
import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.service.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private static final String TRY_AGAIN_MESSAGE = "\nTry again\n";

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Optional<User> authenticate(BufferedReader reader, Class<? extends User> userType) {
        while (true) {
            String nameInput;
            String passwordInput;
            try {
                nameInput = InputValidator.getInputSupplier(reader, "^[a-zA-Zа-яА-Я0-9]{1,20}$")
                        .supplier("Enter your name, please.");
                passwordInput = InputValidator.getInputSupplier(reader, "^[a-zA-Zа-яА-Я0-9]{1,20}$")
                        .supplier(nameInput + ", enter your password, please.");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }

            try {
                Optional<User> possibleUser = authorizationService.authenticate(nameInput, passwordInput, userType);
                USER_OUTPUT_LOGGER.info("You have successfully logged in.");
                return possibleUser;
            } catch (UserNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }
}