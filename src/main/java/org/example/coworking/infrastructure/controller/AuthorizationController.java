package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.User;
import org.example.coworking.service.AuthorizationService;
import org.example.coworking.service.AuthorizationServiceImpl;
import org.example.coworking.service.UserService;
import org.example.coworking.service.UserServiceImpl;
import org.example.coworking.service.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class AuthorizationController {
    protected AuthorizationService authorizationService;
    protected UserService userService;

    public AuthorizationController() {
        this.authorizationService = new AuthorizationServiceImpl();
        this.userService = new UserServiceImpl();
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
                writer.write("User with the name " + name + " doesn't exist.\nTry again\n");
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
