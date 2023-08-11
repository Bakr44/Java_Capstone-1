package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

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
    private Integer price;

    @NotEmpty(message = "CategoryID can't be empty")
    private String CategoryID;

    //Extra credit

    private ArrayList<ProductReview> reviews = new ArrayList<>();

    private double averageRating;

}
