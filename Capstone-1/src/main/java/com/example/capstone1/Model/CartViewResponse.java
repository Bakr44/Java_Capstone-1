package com.example.capstone1.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CartViewResponse {
    private String username;
    private ArrayList<CartItemResponse> cartItems;
    private Integer totalCost;
}
