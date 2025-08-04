package com.juanliz.protecciontest.controller;

import com.juanliz.protecciontest.dto.UserGetDto;
import com.juanliz.protecciontest.dto.UserUpdateDto;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(
        name = "Usuarios",
        description = "Operaciones para la gestión de usuarios del sistema. Solo accesible para administradores."
)
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario en el sistema."
    )
    public ResponseEntity<UserGetDto> createUser(@Valid User user) {
        UserGetDto createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente en el sistema."
    )
    public ResponseEntity<UserGetDto> updateUser(@Valid UserUpdateDto user) {
        UserGetDto updatedUser = userService.updateUser(user);
        return updatedUser == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(updatedUser);
    }

}
