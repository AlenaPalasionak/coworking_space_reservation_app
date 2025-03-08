package org.example.coworking.service.exception;

import org.example.coworking.model.User;

public class ForbiddenActionException extends Exception {

    public ForbiddenActionException(Class<? extends User> user) {
        super("Action is forbidden for the user: " + user.getSimpleName() + ". \n");
    }
}
