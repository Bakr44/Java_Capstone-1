package com.example.capstone1.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReview {
//Extra credit
    private String reviewerName;

    @Min(value = 0,message = "Should be between 0-5")
    @Max(value = 5,message = "Should be between 0-5")
    private int rating;

    private String reviewText;
}
