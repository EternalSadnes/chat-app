package com.eternal.chatapp.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public static RoleNotFoundException fromRoleName(String roleName) {
        return new RoleNotFoundException(String.format("Role with name '%s' has not been found.", roleName));
    }
}
