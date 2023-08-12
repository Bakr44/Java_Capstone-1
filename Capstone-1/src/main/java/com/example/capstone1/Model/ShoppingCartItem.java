package com.example.capstone1.Model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingCartItem {

    private Product product;

    @Positive(message = "Quantity should be more than zero")
    private Integer quantity;
}
