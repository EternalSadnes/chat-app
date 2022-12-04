package com.eternal.chatapp.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public static UserAlreadyExistException fromUsername(String username) {
        return new UserAlreadyExistException(String.format("User with username '%s' already exist.", username));
    }
}
