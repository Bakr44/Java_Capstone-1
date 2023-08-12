package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Service.MerchantService;
import com.example.capstone1.Service.MerchantStockService;
import com.example.capstone1.Service.ProductService;
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
    private final MerchantService merchantService;
    private final ProductService productService;

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
        Merchant merchant=merchantService.getMerchantByID(merchantStock.getMerchantID());
        if (merchant==null){
            return ResponseEntity.status(400).body(new ApiResponse("Merchant ID not Found"));
        }
        Product product=productService.getProductById(merchantStock.getProductID());
        if (product==null){
            return ResponseEntity.status(400).body(new ApiResponse("Product ID not Found"));
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

    @PostMapping("/addMoreStocks")
    public ResponseEntity addMoreStocksToMerchantStock(@RequestBody @Valid MerchantStock merchantStock,Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if(merchantStockService.isAddMoreStocks(merchantStock)){
            return ResponseEntity.status(200).body(new ApiResponse("Stocks Added Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found for the given product and merchant ID"));
    }





    //return MerchantStock by MerchantStockID
    @GetMapping("/search/{id}")
    public ResponseEntity searchMerchantStock(@PathVariable Integer id){
        MerchantStock isSearched=merchantStockService.searchID(id);
        if (isSearched==null){
            return ResponseEntity.status(400).body(new ApiResponse("Sorry Merchant Stock Not Found!!"));
        }
        return ResponseEntity.status(200).body(isSearched);
    }

    @GetMapping("/sort")
    public ResponseEntity sortMerchantStock(){
        return ResponseEntity.status(200).body(merchantStockService.sortMerchantStock());

    }


}
