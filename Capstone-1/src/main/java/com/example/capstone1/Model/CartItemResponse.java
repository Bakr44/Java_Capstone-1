package com.example.capstone1.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Integer productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private double itemTotal;
}
