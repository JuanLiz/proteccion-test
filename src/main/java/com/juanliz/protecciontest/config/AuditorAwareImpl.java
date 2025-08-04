package com.juanliz.protecciontest.config;

import com.juanliz.protecciontest.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return Optional.of(username);
    }
}

