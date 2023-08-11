package com.example.capstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotNull(message = "ID of category can't be empty")
    private Integer id;

    @NotEmpty(message = "Name of category can't be empty")
    @Size(min = 3,message = "Name of category must be more than 3 characters")
    private String name;


}
