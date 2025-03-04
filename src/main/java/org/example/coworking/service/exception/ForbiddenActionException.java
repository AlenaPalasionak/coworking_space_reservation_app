package org.example.coworking.service.exception;

import org.example.coworking.model.User;

public class ForbiddenActionException extends Exception {
    protected Class<? extends User> user;

    public ForbiddenActionException(Class<? extends User> user) {
        super("Action is forbidden for the user: " + user.getSimpleName() + ". \n");
        this.user = user;
    }
}
