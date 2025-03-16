package org.example.coworking.controller;

import org.example.coworking.controller.exception.InvalidInputException;
import org.example.coworking.controller.validator.InputValidator;
import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.dao.exception.UserNotFoundException;

import java.io.BufferedReader;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private static final String USER_NAME_PATTERN = "^[a-zA-Zа-яА-Я0-9]{1,20}$";
    private static final String USER_PASSWORD_PATTERN = "^[a-zA-Zа-яА-Я0-9]{1,20}$";

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public User authenticate(BufferedReader reader, Class<? extends User> userType) {
        while (true) {
            String nameInput;
            String passwordInput;
            try {
                nameInput = InputValidator.getInputSupplier(reader, USER_NAME_PATTERN)
                        .supplier("Enter your name, please.");
                passwordInput = InputValidator.getInputSupplier(reader, USER_PASSWORD_PATTERN)
                        .supplier(nameInput + ", enter your password, please.");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }

            try {
                User user = authorizationService.authenticate(nameInput, passwordInput, userType);
                USER_OUTPUT_LOGGER.info("You have successfully logged in.");
                return user;
            } catch (UserNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }
}