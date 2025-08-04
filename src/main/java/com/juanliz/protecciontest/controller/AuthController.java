package com.juanliz.protecciontest.controller;

import com.juanliz.protecciontest.auth.AuthRequest;
import com.juanliz.protecciontest.auth.AuthResponse;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Autenticación",
        description = "Operaciones para la autenticación de usuarios")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<User> me() {
        User user = userService.getCurrentAuthenticatedUser();
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}