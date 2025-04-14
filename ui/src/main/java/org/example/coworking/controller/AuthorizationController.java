package org.example.coworking.controller;

import org.example.coworking.controller.exception.InvalidInputException;
import org.example.coworking.controller.validator.InputValidator;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;

import java.io.BufferedReader;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

/**
 * This class handles user authentication using the {@link AuthorizationService}.
 * It validates the user's input for name and password, and then attempts to authenticate the user.
 * If authentication fails, the user is prompted to enter the credentials again.
 */
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private static final String USER_NAME_PATTERN = "^[a-zA-Zа-яА-Я0-9]{1,20}$";
    private static final String USER_PASSWORD_PATTERN = "^[a-zA-Zа-яА-Я0-9]{1,20}$";

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * Prompts the Customer to enter their name and password, validates the input,
     * and attempts to authenticate the user.
     * If the input is invalid or the authentication fails, the Customer is asked to try again.
     *
     * @param reader the {@link BufferedReader} to read Customer input
     * @param role   the class type of the user to be authenticated
     * @return the authenticated {@link Customer} object if authentication is successful
     */
    public <T extends User> T authenticate(BufferedReader reader, Class<T> role) {
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
                T user = authorizationService.authenticate(nameInput, passwordInput, role);
                USER_OUTPUT_LOGGER.info("You have successfully logged in.");
                return user;

            } catch (EntityNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }
}
