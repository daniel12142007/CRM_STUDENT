package com.kaitech.student_crm.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        return JWT.create()
                .withSubject("User details")
                .withIssuedAt(new Date())
                .withClaim("username", username)
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMonths(1).toInstant()))
                .withIssuer("daniel_tamoe")
                .sign(Algorithm.HMAC512(secret));
    }

    public String checkToken(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(secret))
                .withIssuer("daniel_tamoe")
                .withSubject("User details")
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT.getClaim("username").asString();
    }
}