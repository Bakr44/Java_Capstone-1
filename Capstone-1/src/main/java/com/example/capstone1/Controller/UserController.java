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




    @PostMapping("/buy")
    public ResponseEntity buyProduct(@RequestParam Integer userID,@RequestParam Integer productId, @RequestParam Integer merchantId){

        User user=userService.getUserById(userID);
        if (user==null){
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        MerchantStock merchantStock = userService.getMerchantStockProductIdMerchantIdInUser(productId, merchantId);
        if (merchantStock == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid product ID or merchant ID"));
        }
        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));
        }
        Product product = userService.getProductByIdInUser(productId);
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
        userService.reduceStockInUser(productId,merchantId);

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

    @PostMapping("/add-to-cart")
    public ResponseEntity addToCart(@RequestParam Integer userID, @RequestParam Integer productId, @RequestParam  Integer quantity) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        Product product = userService.getProductByIdInUser(productId);
        if (product == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid product ID"));
        }
        if (quantity<=0){
            return ResponseEntity.status(400).body(new ApiResponse("Quantity must be greater than zero"));
        }
        MerchantStock merchantStock=userService.getMerchantStockByProductIdInUser(productId);
        if (merchantStock == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found for the product"));
        }
        if (quantity > merchantStock.getStock()) {
            return ResponseEntity.status(400).body(new ApiResponse("The quantity is not available"));
        }

        // Reduce the stock
        userService.reduceStockInUser(productId, merchantStock.getMerchantID(), quantity);


        // Add the product to the user's shopping cart
        userService.addItemInUser(product, quantity);

        return ResponseEntity.status(200).body(new ApiResponse("Product added to cart"));
    }

    @PostMapping("/remove-from-cart")
    public ResponseEntity removeFromCart(@RequestParam Integer userID, @RequestParam Integer productId, @RequestParam Integer quantity) {
        User user=userService.getUserById(userID);
        if (user==null){
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }
        Product product = userService.getProductByIdInUser(productId);
        if (product == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        userService.removeItemInUser(product);

        return ResponseEntity.status(200).body(new ApiResponse("Product removed from cart"));
    }

    @GetMapping("/view-cart")
    public ResponseEntity viewCart(@RequestParam Integer userID) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }

        CartViewResponse cartViewResponse=userService.cartViewResponse(userID);

        return ResponseEntity.status(200).body(cartViewResponse);
        // Return ResponseEntity with cart items
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(@RequestParam(required = false) Integer userID,  @RequestParam String discountName,
                                   @RequestParam String couponCode) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid user"));
        }

        Double totalCost = userService.calculateTotalCostInUser();
        Double oldtotalCost = userService.calculateTotalCostInUser();

        if (user.getBalance() < totalCost) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        boolean isCartItemEmpty=userService.isCartEmptyInuser();
        if (isCartItemEmpty){
            return ResponseEntity.status(400).body(new ApiResponse("Cart is empty"));
        }

        // Initialize variables to store discount and coupon details
      Double newtotalCost=userService.totalCostInUser(discountName,couponCode,totalCost);

        // Deduct the total cost from user's balance
        user.deductBalance(newtotalCost);

        // Clear the shopping cart
        userService.clearCartInUser();

        // Create an invoice object with applied discount and coupon details
        Invoice invoice=userService.generateInvoice(discountName,couponCode,newtotalCost);
        // Return success message along with invoice details
        String message = "Checkout successful. Total cost: " + oldtotalCost;
        return ResponseEntity.status(200).body(message + "\n" + invoice.toString());
    }

    }



