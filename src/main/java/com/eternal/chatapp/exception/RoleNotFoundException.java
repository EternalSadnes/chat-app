package com.eternal.chatapp.exception;

import com.eternal.chatapp.model.UserRole;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public static RoleNotFoundException fromRoleName(UserRole userRole) {
        return new RoleNotFoundException(String.format("Role with name '%s' has not been found.", userRole));
    }
}
