package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

@GetMapping("/get")
public ResponseEntity getAllMerchant(){
    return ResponseEntity.status(200).body(merchantService.getAllMerchant());
}

@PostMapping("/add")
public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors){
    if (errors.hasErrors()){
        String message=errors.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(message);
    }
    merchantService.addMerchants(merchant);
    return ResponseEntity.status(200).body(new ApiResponse("Merchant Added Successfully"));
}


    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant(@PathVariable Integer id, @RequestBody @Valid Merchant merchant, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated=merchantService.updateMerchant(id,merchant);
        if (isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Updated Successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant Not Found!!"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMerchant(@PathVariable Integer id){
    boolean isDeleted=merchantService.isDeleted(id);
    if (isDeleted){
        return ResponseEntity.status(200).body(new ApiResponse("Merchant Deleted Successfully"));
    }return ResponseEntity.status(400).body(new ApiResponse("Merchant Not Found!!"));
    }


    //return Product by his name
    @GetMapping("/search/{name}")
    public ResponseEntity searchMerchant(@PathVariable String name){
        Merchant isSearched=merchantService.searchName(name);
        if (isSearched==null){
            return ResponseEntity.status(400).body(new ApiResponse("Sorry Merchant Not Found!!"));
        }
        return ResponseEntity.status(200).body(isSearched);
    }

    @GetMapping("/sort")
    public ResponseEntity sortMerchant(){
        return ResponseEntity.status(200).body(merchantService.sortMerchant());

    }

}
