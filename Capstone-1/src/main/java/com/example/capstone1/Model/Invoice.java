package com.example.capstone1.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoice {
    private String invoiceNumber;
    private double totalCost;
    private Coupon appliedCoupon;
    private Discount appliedDiscount;
    private double discountedTotalCost;
}
