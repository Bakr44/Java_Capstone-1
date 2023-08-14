package com.example.capstone1.Service;

import com.example.capstone1.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MerchantStockService merchantStockService;
    private final ProductService productService;
    private final DiscountService discountService;
    private final CouponService couponService;
    private final ShoppingCartItemService shoppingCartItemService;

    ArrayList<User> users=new ArrayList<>();

    public ArrayList<User> getAllUser(){
        return users;
    }

    public User getUserById(Integer userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null; // Return null if no user with the given ID is found
    }

    public void addUser(User user){
        users.add(user);
    }

    public boolean isUpdated(Integer id,User user){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)){
                users.set(i,user);
                return true;
            }
        }return false;
    }

    public boolean isDeleted(Integer id){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)){
                users.remove(i);
                return true;
            }
        }return false;
    }


    public User searchName(String name){
        for (User user:users) {
            if (user.getUserName().equalsIgnoreCase(name)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> sortUser(){
        ArrayList<User> sortedUser=new ArrayList<>();
        for (User user:users) {
            sortedUser.add(user);
        }
        Comparator<User> comparatorName=Comparator.comparing(User::getUserName);
        Collections.sort(sortedUser,comparatorName);
        return sortedUser;
    }


    public MerchantStock getMerchantStockProductIdMerchantIdInUser(Integer productId, Integer merchantId){
        MerchantStock merchantStock=merchantStockService.getMerchantStockProductIdMerchantId(productId, merchantId);
        return merchantStock;
    }
    public void reduceStockInUser(Integer productId, Integer merchantId){
        merchantStockService.reduceStock(productId, merchantId);

    }

    public void reduceStockInUser(Integer productId, Integer merchantId,Integer quantity){
        merchantStockService.reduceStock(productId, merchantId,quantity);

    }

    public MerchantStock getMerchantStockByProductIdInUser(Integer productId){
        MerchantStock merchantStock=merchantStockService.getMerchantStockByProductId(productId);
        return merchantStock;
    }


    public Product getProductByIdInUser(Integer productId){
        Product product=productService.getProductById(productId);
        return product;
    }

    public Discount getDiscountByNameInUser(String name){
        return discountService.getDiscountByName(name);
    }

    public Double calculateDiscountedPriceInUser(double originalPrice, String discountName){
        return discountService.calculateDiscountedPrice(originalPrice,discountName);
    }

    public Coupon getCouponByCodeInUser(String name){
        return couponService.getCouponByCode(name);
    }

    public Double calculateCouponPriceInUser(double originalPrice, String couponCode){
        return couponService.calculateCouponPrice(originalPrice,couponCode);
    }

   public void addItemInUser(Product product, Integer quantity){
        shoppingCartItemService.addItem(product,quantity);

   }

    public void removeItemInUser(Product product){
        shoppingCartItemService.removeItem(product);

    }

    public ArrayList<ShoppingCartItem> getItemsInUser(){
        return shoppingCartItemService.getItems();
    }

    public Double calculateTotalCostInUser(){
       return shoppingCartItemService.calculateTotalCost();
    }

    public boolean isCartEmptyInuser(){
        return shoppingCartItemService.isCartEmpty();
    }

    public void clearCartInUser() {
        shoppingCartItemService.clearCart();
    }

public CartViewResponse cartViewResponse(Integer userID){
        User user=getUserById(userID);
    ArrayList<ShoppingCartItem> cartItems = getItemsInUser();

    // Create a response object or DTO to send cart items
    ArrayList<CartItemResponse> cartItemResponses = new ArrayList<>();
    for (ShoppingCartItem cartItem : cartItems) {
        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();
        double itemTotal = product.getPrice() * quantity;

        CartItemResponse cartItemResponse = new CartItemResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity,
                itemTotal
        );
        cartItemResponses.add(cartItemResponse);
    }

    CartViewResponse cartViewResponse = new CartViewResponse(
            user.getUserName(),
            cartItemResponses,
            calculateTotalCostInUser()
    );
    return cartViewResponse;
}


    public Double totalCostInUser(String discountName,String couponCode, Double totalCost){
        Discount appliedDiscount = null;
        Double discountAmount = 0.0;
        Coupon appliedCoupon = null;
        Double couponAmount = 0.0;

        // Apply discount if discountName is provided
        if (discountName != null) {
            appliedDiscount = getDiscountByNameInUser(discountName);
            if (appliedDiscount != null) {
                discountAmount = calculateDiscountedPriceInUser(totalCost, discountName);
                totalCost -= discountAmount;
            }
        }

        // Apply coupon if couponCode is provided
        if (couponCode != null) {
            appliedCoupon = getCouponByCodeInUser(couponCode);
            if (appliedCoupon != null) {
                couponAmount =calculateCouponPriceInUser(totalCost, couponCode);
                totalCost -= couponAmount;
            }
        }
        return totalCost;
    }

    public Invoice generateInvoice(String discountName, String couponCode, Double originalTotalCost) {
        Discount appliedDiscount = null;
        Double discountAmount = 0.0;
        Coupon appliedCoupon = null;
        Double couponAmount = 0.0;

        // Apply discount if discountName is provided
        if (discountName != null) {
            appliedDiscount = getDiscountByNameInUser(discountName);
            if (appliedDiscount != null) {
                discountAmount = calculateDiscountedPriceInUser(originalTotalCost, discountName);
                originalTotalCost -= discountAmount;
            }
        }

        // Apply coupon if couponCode is provided
        if (couponCode != null) {
            appliedCoupon = getCouponByCodeInUser(couponCode);
            if (appliedCoupon != null) {
                couponAmount = calculateCouponPriceInUser(originalTotalCost, couponCode);
                originalTotalCost -= couponAmount;
            }
        }

        // Create the Invoice object with applied discount and coupon details
        return new Invoice("INVOICE", appliedCoupon, appliedDiscount, originalTotalCost);
    }



}
