package com.eternal.chatapp.service;

import com.eternal.chatapp.model.User;

public interface JwtTokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String parseToken(String token);
}
