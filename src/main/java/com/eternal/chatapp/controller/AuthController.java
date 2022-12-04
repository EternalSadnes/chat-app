package com.eternal.chatapp.controller;

import com.eternal.chatapp.dto.AuthRequestDto;
import com.eternal.chatapp.model.User;
import com.eternal.chatapp.service.JwtTokenService;
import com.eternal.chatapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserDetailsService usrDetailsService;

    private final UserService userService;

    record LoginRequest(String username, String password) {
    }

    @PostMapping("/sign-in")
    public TokensResponse login(@RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username, request.password);
        authManager.authenticate(authenticationToken);

        var user = (User) usrDetailsService.loadUserByUsername(request.username);
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new TokensResponse(accessToken, refreshToken);
    }

    record SignUpRequest(String username, String password) {
    }

    @PostMapping("/sign-up")
    public TokensResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        User registeredUser = userService.register(new AuthRequestDto(signUpRequest.username(), signUpRequest.password()));

        String accessToken = tokenService.generateAccessToken(registeredUser);
        String refreshToken = tokenService.generateRefreshToken(registeredUser);

        return new TokensResponse(accessToken, refreshToken);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/token/refresh")
    public TokensResponse refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String previousRefreshToken = headerAuth.substring(7);

        String username = tokenService.parseToken(previousRefreshToken);
        var user = (User) usrDetailsService.loadUserByUsername(username);
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new TokensResponse(accessToken, refreshToken);
    }

    record TokensResponse(String accessToken, String refreshToken) {
    }

}
