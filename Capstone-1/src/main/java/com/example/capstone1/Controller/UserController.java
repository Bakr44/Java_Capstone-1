package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Service.MerchantService;
import com.example.capstone1.Service.MerchantStockService;
import com.example.capstone1.Service.ProductService;
import com.example.capstone1.Service.UserService;
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
            return ResponseEntity.status(400).body("Invalid product ID or merchant ID");
        }
        if (merchantStock.getStock() <= 0) {
            return ResponseEntity.status(400).body("Product is out of stock");
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(400).body("Product not found"); // Handle this case
        }
        Integer productPrice =product.getPrice();
//        Integer productPrice = productService.getProductById(merchantStock.getProductID()).getPrice();
        if (user.getBalance() < productPrice) {
            return ResponseEntity.status(400).body("Insufficient balance");
        }
        // Deduct the price from user balance
        user.deductBalance(productPrice);

        // Reduce the stock from MerchantStock
        merchantStockService.reduceStock(productId,merchantId);

        return ResponseEntity.status(200).body("Product Purchased Successfully");
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

}
