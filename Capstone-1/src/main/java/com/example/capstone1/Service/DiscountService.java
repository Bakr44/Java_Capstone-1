package com.example.capstone1.Service;

import com.example.capstone1.Model.Discount;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DiscountService {
    ArrayList<Discount>discounts=new ArrayList<>();

    public DiscountService(){
        discounts.add(new Discount("NationalDay", 65));
        discounts.add(new Discount("EidDiscount", 30));
    }

    public Discount getDiscountByName(String name) {
        return discounts.stream()
                .filter(discount -> discount.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public double calculateDiscountedPrice(double originalPrice, String discountName) {
        Optional<Discount> discountOptional = discounts.stream()
                .filter(discount -> discount.getName().equals(discountName))
                .findFirst();

        if (discountOptional.isPresent()) {
            Discount discount = discountOptional.get();
            double discountPercentage = discount.getPercentage();
            return originalPrice - (originalPrice * (discountPercentage / 100));
        }

        return originalPrice;
    }




}

