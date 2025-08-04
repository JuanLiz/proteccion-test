package com.juanliz.protecciontest.dto;

import com.juanliz.protecciontest.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.juanliz.protecciontest.model.User}
 */
@Value
public class UserGetDto implements Serializable {
    int id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String username;
    @NotNull
    UserRole userRole;
}