package com.eternal.chatapp.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.eternal.chatapp.model.User;
import com.eternal.chatapp.service.JwtTokenService;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.chat.jwt.access-token.expiration}")
    private int accessTokenExpiration;

    @Value("${app.chat.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        String[] scope = user.getAuthorities().stream()
                .map(grantedAuthority -> StringUtils.substringAfter(grantedAuthority.getAuthority(), "ROLE_"))
                .toArray(String[]::new);


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiration, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        String scope = "REFRESH_TOKEN";

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiration, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String parseToken(String token) {
        try {
            SignedJWT decodedJWT = SignedJWT.parse(token);
            return decodedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
