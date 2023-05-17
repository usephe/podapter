package com.audiophileproject.usermanagement.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @Nonnull
    private String username;
    @Nonnull
    private String email;
    @Nonnull
    private String password;
}
