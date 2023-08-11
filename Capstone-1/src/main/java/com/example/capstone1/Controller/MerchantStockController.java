package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchantstock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;


    @GetMapping("/get")
    public ResponseEntity getAllMerchantStock(){
        return ResponseEntity.status(200).body(merchantStockService.getAllMerchantStock());


    }

    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        merchantStockService.addMerchantStock(merchantStock);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock Added Successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable Integer id,@RequestBody @Valid MerchantStock merchantStock,Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated= merchantStockService.isUpdated(id,merchantStock);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock Updated Successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable Integer id){
        boolean isDeleted=merchantStockService.isDelete(id);
        if (isDeleted){
            return ResponseEntity.status(200).body("Merchant Stock Delete Successfully");
        }return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock Not Found!!"));
    }




}
