package com.eternal.chatapp.controller;

import com.eternal.chatapp.dto.AuthRequestDto;
import com.eternal.chatapp.model.CustomUsrDetails;
import com.eternal.chatapp.model.User;
import com.eternal.chatapp.service.JwtTokenService;
import com.eternal.chatapp.service.UserService;
import com.eternal.chatapp.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl usrDetailsService;

    private final UserService userService;

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String message, String access_jwt_token, String refresh_jwt_token) {
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username, request.password);
        Authentication auth = authManager.authenticate(authenticationToken);

        CustomUsrDetails user = (CustomUsrDetails) usrDetailsService.loadUserByUsername(request.username);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new LoginResponse("User with email = " + request.username + " successfully logined!"

                , access_token, refresh_token);
    }

    @PostMapping("/register")
    public RefreshTokenResponse register(@RequestBody LoginRequest loginRequest) {
        User registered = userService.register(new AuthRequestDto(loginRequest.username(), loginRequest.password()));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(registered.getEmail(), registered.getPassword());
        Authentication auth = authManager.authenticate(authenticationToken);

        CustomUsrDetails user = (CustomUsrDetails) usrDetailsService.loadUserByUsername(loginRequest.username);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new RefreshTokenResponse(access_token, refresh_token);
    }

    record RefreshTokenResponse(String access_jwt_token, String refresh_jwt_token) {
    }

    @GetMapping("/token/refresh")
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String refreshToken = headerAuth.substring(7, headerAuth.length());

        String email = tokenService.parseToken(refreshToken);
        CustomUsrDetails user = (CustomUsrDetails) usrDetailsService.loadUserByUsername(email);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new RefreshTokenResponse(access_token, refresh_token);
    }

}
