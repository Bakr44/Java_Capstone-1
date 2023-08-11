package com.example.capstone1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotNull(message = "ID of Merchant Stock can't be empty")
    private Integer id;

    @NotNull(message = "Product ID of Merchant Stock can't be empty")
    private Integer productID;

    @NotNull(message = "Merchant ID of Merchant Stock can't be empty")
    private Integer  merchantID;

    @NotNull(message = "Stock of Merchant can't be empty")
    @Min(value = 10,message = "Stock should be more than 10")
    private Integer stock;

}
