package com.eternal.chatapp.service;

import com.eternal.chatapp.dto.AuthRequestDto;
import com.eternal.chatapp.exception.UserAlreadyExistException;
import com.eternal.chatapp.model.User;

public interface UserService {
    User register(AuthRequestDto authRequestDto) throws UserAlreadyExistException;

    boolean isUserExist(String username);
}
