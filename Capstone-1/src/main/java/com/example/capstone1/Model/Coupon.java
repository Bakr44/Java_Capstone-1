package com.example.capstone1.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coupon {
    private String code;
    private double discountAmount;

    public double calculateDiscount(double amount) {
        return Math.min(amount, discountAmount);
    }
}
