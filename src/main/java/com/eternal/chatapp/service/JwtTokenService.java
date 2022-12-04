package com.eternal.chatapp.service;

import com.eternal.chatapp.model.CustomUsrDetails;

public interface JwtTokenService {
    String generateAccessToken(CustomUsrDetails usrDetails);

    String generateRefreshToken(CustomUsrDetails usrDetails);

    String parseToken(String token);
}
