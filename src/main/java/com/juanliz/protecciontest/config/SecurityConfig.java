package com.juanliz.protecciontest.config;

import com.juanliz.protecciontest.config.securityFilter.JwtSecFilter;
import com.juanliz.protecciontest.repository.UserRepository;
import com.juanliz.protecciontest.service.UserService;
import com.juanliz.protecciontest.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public SecurityConfig(
            JwtUtils jwtUtils,
            UserRepository userRepository
    ) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(r -> {
                    r.requestMatchers(HttpMethod.GET, "/auth/**").permitAll();
                    r.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    r.requestMatchers(HttpMethod.PUT, "/auth/**").permitAll();
                    r.requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();
                    r.requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll();
                    r.requestMatchers(HttpMethod.GET, "/v3/api-docs.yaml").permitAll();
                    // Unspecified
                    r.anyRequest().authenticated();
                })
                .addFilterBefore(
                        new JwtSecFilter(jwtUtils, userRepository),
                        BasicAuthenticationFilter.class
                ).build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}