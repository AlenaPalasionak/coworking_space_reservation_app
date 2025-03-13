package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class MenuNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public MenuNotFoundException(String message) {
        super(message);
        this.errorCode = DaoErrorCode.MENU_NOT_FOUND;
    }
}
