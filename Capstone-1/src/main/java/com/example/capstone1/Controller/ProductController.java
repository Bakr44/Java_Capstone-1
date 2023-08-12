package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.ProductReview;
import com.example.capstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity getAllProduct(){
        return ResponseEntity.status(200).body(productService.getAllProduct());
    }

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        productService.addProduct(product);
        return ResponseEntity.status(200).body(new ApiResponse("Product Added"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id,@RequestBody @Valid Product product,Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated=productService.updateProduct(id,product);
        if (isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Product Updated Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Product Not Found!!"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id){
        boolean isDeleted=productService.isDeleted(id);
        if (isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("Product Deleted Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Not Found!!"));
    }

    //return Product by his name
    @GetMapping("/search/{name}")
    public ResponseEntity searchProduct(@PathVariable String name){
        Product isSearched=productService.searchName(name);
        if (isSearched==null){
            return ResponseEntity.status(400).body(new ApiResponse("Sorry Product Not Found!!"));
        }
        return ResponseEntity.status(200).body(isSearched);
    }

    @GetMapping("/sort")
    public ResponseEntity sortProduct(){
        return ResponseEntity.status(200).body(productService.sortProduct());

    }

    @PostMapping("/{productId}/review")
    public ResponseEntity addProductReview(@PathVariable Integer productId,@RequestBody @Valid ProductReview productReview,Errors errors) {
      if (errors.hasErrors()){
          String message=errors.getFieldError().getDefaultMessage();
          return ResponseEntity.status(400).body(message);
      }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(400).body("Product not found");
    }
        ProductReview newReview = new ProductReview(productReview.getReviewerName(), productReview.getRating(), productReview.getReviewText());
//        if (product.getReviews()==null){
//            return ResponseEntity.status(400).body("Product not found");
//        }
        if (product.getReviews() == null) {
            product.setReviews(new ArrayList<>()); // Initialize the reviews list
        }
        product.getReviews().add(newReview);

        double totalRating = product.getReviews().stream()
                .mapToInt(ProductReview::getRating).sum();

        int totalReviews = product.getReviews().size();
        double averageRating = (double) totalRating / totalReviews;
        product.setAverageRating(averageRating);

        return ResponseEntity.status(200).body("Review added successfully");
    }


}
