package com.juanliz.protecciontest.config.securityFilter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.repository.UserRepository;
import com.juanliz.protecciontest.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class JwtSecFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtSecFilter(
            JwtUtils jwtUtils,
            UserRepository userRepository
    ) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            DecodedJWT jwt = jwtUtils.decodeToken(token);
            String userName = jwt.getSubject();
            int userId = Integer.parseInt(jwt.getClaim("id").asString());

            User user = userRepository.findByUsername(userName).orElse(null);

            if (user != null && user.getId() == userId) {
                SimpleGrantedAuthority role = new SimpleGrantedAuthority(jwt.getClaim("role").asString());

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userName,
                        userId,
                        Set.of(role)
                );
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }

        }

        filterChain.doFilter(request, response);
    }
}