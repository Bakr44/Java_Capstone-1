package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Category;
import com.example.capstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity getAllCategroy(){
        return ResponseEntity.status(200).body(categoryService.getAllCategory());
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Added Successfully!!"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable Integer id ,@RequestBody @Valid Category category,Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated=categoryService.isUpdated(id, category);
        if (isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Category Updated Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not Found!!"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id){
        boolean isDeleted=categoryService.isDeleted(id);
        if (isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("Category Deleted Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category Not Found!!"));
    }

    //return Product by his name
    @GetMapping("/search/{name}")
    public ResponseEntity searchCategory(@PathVariable String name){
        Category isSearched=categoryService.searchName(name);
        if (isSearched==null){
            return ResponseEntity.status(400).body(new ApiResponse("Sorry Category Not Found!!"));
        }
        return ResponseEntity.status(200).body(isSearched);
    }

    @GetMapping("/sort")
    public ResponseEntity sortCategory(){
        return ResponseEntity.status(200).body(categoryService.sortCategory());

    }
}
