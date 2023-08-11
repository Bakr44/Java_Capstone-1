package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.SplittableRandom;

@Data
@AllArgsConstructor
public class User {

    @NotNull(message = "ID User can't be empty")
    private Integer id;

    @NotEmpty(message = "Name of user can't be empty")
    @Size(min = 5,message = "User name should be more than 5 characters")
    private String userName;

    @NotEmpty(message = "Password can't be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    private String password;

    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;

    @NotEmpty(message = "Role can't be empty")
    @Pattern(regexp = "Admin|Customer",message = "Position should be Admin OR Customer")
    private String role;

    @NotNull(message = "Balance of user can't be empty")
    @Positive(message = "Balance can't be negative ")
    private Integer balance;
}
