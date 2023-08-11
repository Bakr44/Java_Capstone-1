package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.EnableMBeanExport;

@Data
@AllArgsConstructor
public class Product {

    @NotNull(message = "ID must not be empty")
    private Integer id;

    @NotEmpty(message = "Name can't be empty")
    @Size(min = 3,message = "Name must be more than 3 characters")
    private String name;

    @NotNull(message = "Price can't be empty")
    @PositiveOrZero(message = "Price can't be negative number")
    private Double price;

    @NotEmpty(message = "CategoryID can't be empty")
    private String CategoryID;


}
