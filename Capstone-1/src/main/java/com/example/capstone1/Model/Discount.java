package com.example.capstone1.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Discount {
    private String name;
    private double percentage;

    public double calculateDiscount(Double amount) {
        return amount * (percentage / 100.0);
    }
}
