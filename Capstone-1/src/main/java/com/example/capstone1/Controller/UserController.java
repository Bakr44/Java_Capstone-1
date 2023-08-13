package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.*;
import com.example.capstone1.Service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity getAllUser(){
        return ResponseEntity.status(200).body(userService.getAllUser());
    }
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String mesaage=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(mesaage);
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User Added Successfully"));
}

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated=userService.isUpdated(id,user);
        if (isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("User Updated Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User Not Found!!"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        boolean isDeleted=userService.isDeleted(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("User Delete Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User Not Found!!"));
        }


    private final MerchantStockService merchantStockService;
    private final ProductService productService;

    @PostMapping("/buy")
    public ResponseEntity buyProduct(@RequestParam Integer userID,@RequestParam Integer productId, @RequestParam Integer merchantId){

        User user=userService.getUserById(userID);
        if (user==null){
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        MerchantStock merchantStock = merchantStockService.getMerchantStockProductIdMerchantId(productId, merchantId);
        if (merchantStock == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid product ID or merchant ID"));
        }
        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        Integer productPrice =product.getPrice();
//        Integer productPrice = productService.getProductById(merchantStock.getProductID()).getPrice();
        if (user.getBalance() < productPrice) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        // Deduct the price from user balance
        user.deductBalance(Double.valueOf(productPrice));

        // Reduce the stock from MerchantStock
        merchantStockService.reduceStock(productId,merchantId);

        return ResponseEntity.status(200).body(new ApiResponse("Product Purchased Successfully"));
    }


    //return Product by his username
    @GetMapping("/search/{name}")
    public ResponseEntity searchUser(@PathVariable String name){
        User isSearched=userService.searchName(name);
        if (isSearched==null){
            return ResponseEntity.status(400).body(new ApiResponse("Sorry User Not Found!!"));
        }
        return ResponseEntity.status(200).body(isSearched);
    }

    @GetMapping("/sort")
    public ResponseEntity sortUser(){
        return ResponseEntity.status(200).body(userService.sortUser());

    }

    private final ShoppingCartItemService shoppingCartItemService;

    @PostMapping("/add-to-cart")
    public ResponseEntity addToCart(@RequestParam Integer userID, @RequestParam Integer productId, @RequestParam  Integer quantity) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid product ID"));
        }
        if (quantity<=0){
            return ResponseEntity.status(400).body(new ApiResponse("Quantity must be greater than zero"));
        }
        MerchantStock merchantStock=merchantStockService.getMerchantStockByProductId(productId);
        if (merchantStock == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found for the product"));
        }
        if (quantity > merchantStock.getStock()) {
            return ResponseEntity.status(400).body(new ApiResponse("The quantity is not available"));
        }

        // Reduce the stock
        merchantStockService.reduceStock(productId, merchantStock.getMerchantID(), quantity);


        // Add the product to the user's shopping cart
        shoppingCartItemService.addItem(product, quantity);

        return ResponseEntity.status(200).body(new ApiResponse("Product added to cart"));
    }

    @PostMapping("/remove-from-cart")
    public ResponseEntity removeFromCart(@RequestParam Integer userID, @RequestParam Integer productId, @RequestParam Integer quantity) {
        User user=userService.getUserById(userID);
        if (user==null){
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        shoppingCartItemService.removeItem(product);

        return ResponseEntity.status(200).body(new ApiResponse("Product removed from cart"));
    }

    @GetMapping("/view-cart")
    public ResponseEntity viewCart(@RequestParam Integer userID) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        ArrayList<ShoppingCartItem> cartItems = shoppingCartItemService.getItems();

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
                shoppingCartItemService.calculateTotalCost()
        );

        return ResponseEntity.status(200).body(cartViewResponse);
        // Return ResponseEntity with cart items
    }

    private final DiscountService discountService;
    private final CouponService couponService;

    @PostMapping("/checkout")
    public ResponseEntity checkout(@RequestParam(required = false) Integer userID,  @RequestParam String discountName,
                                   @RequestParam String couponCode) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }

        Double totalCost = shoppingCartItemService.calculateTotalCost();
        Double oldtotalCost = shoppingCartItemService.calculateTotalCost();

        if (user.getBalance() < totalCost) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        boolean isCartItemEmpty=shoppingCartItemService.isCartEmpty();
        if (isCartItemEmpty){
            return ResponseEntity.status(400).body(new ApiResponse("Cart is empty"));
        }

        // Initialize variables to store discount and coupon details
        Discount appliedDiscount = null;
        Double discountAmount = 0.0;
        Coupon appliedCoupon = null;
        Double couponAmount = 0.0;

        // Apply discount if discountName is provided
        if (discountName != null) {
            appliedDiscount = discountService.getDiscountByName(discountName);
            if (appliedDiscount != null) {
                discountAmount = discountService.calculateDiscountedPrice(totalCost, discountName);
                totalCost -= discountAmount;
            }
        }

        // Apply coupon if couponCode is provided
        if (couponCode != null) {
            appliedCoupon = couponService.getCouponByCode(couponCode);
            if (appliedCoupon != null) {
                couponAmount = couponService.calculateDiscountedPrice(totalCost, couponCode);
                totalCost -= couponAmount;
            }
        }
        // Deduct the total cost from user's balance
        user.deductBalance(totalCost);

        // Clear the shopping cart
        shoppingCartItemService.clearCart();

//        return ResponseEntity.status(200).body("Checkout successful");
        // Create an invoice object with applied discount and coupon details
        Invoice invoice = new Invoice("INVOICE",oldtotalCost, appliedCoupon, appliedDiscount, totalCost );

        // Return success message along with invoice details
        String message = "Checkout successful. Total cost: " + oldtotalCost + ", Discount: " + discountAmount + ", Coupon: " + couponAmount + ".";
        return ResponseEntity.status(200).body(message + "\n" + invoice.toString());
    }

    }



