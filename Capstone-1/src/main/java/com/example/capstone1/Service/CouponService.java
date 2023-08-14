package com.example.capstone1.Service;

import com.example.capstone1.Model.Coupon;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CouponService {
    ArrayList<Coupon> coupons = new ArrayList<>();

    public CouponService() {
        coupons.add(new Coupon("FirstBuy25", 25));
        coupons.add(new Coupon("NewUser10", 10));
        // Add more coupons
    }

    public Coupon getCouponByCode(String code) {
        return coupons.stream()
                .filter(coupon -> coupon.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    public double calculateCouponPrice(double originalPrice, String couponCode) {
        Optional<Coupon> couponOptional = coupons.stream()
                .filter(coupon -> coupon.getCode().equals(couponCode))
                .findFirst();

        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();
            double discountAmount = coupon.getDiscountAmount();
            return originalPrice - discountAmount;
        }

        return originalPrice;
    }

}
