package com.juanliz.protecciontest.utils;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    private final UserRepository userRepository;

    public JwtUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String userName = authentication.getPrincipal().toString();
        User user = userRepository.findByUsername(userName).orElseThrow();
        String role = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();

        int expirationHours = 8;
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userName)
                .withClaim("id", String.valueOf(user.getId()))
                .withClaim("name", Base64.getEncoder().encodeToString(user.getName().getBytes()))
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (3600000 * expirationHours)))
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid token");
        }
    }

    public int getSubject(String token) {
        return Integer.parseInt(decodeToken(token).getSubject());
    }


}