package com.example.capstone1.Service;

import com.example.capstone1.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

@Service
public class MerchantStockService {


    ArrayList<MerchantStock> merchantStocks=new ArrayList<>();

    public ArrayList<MerchantStock> getAllMerchantStock(){
        return merchantStocks;
    }

    public MerchantStock getMerchantStockProductIdMerchantId(Integer productId,Integer merchantId ){
            return merchantStocks.stream().filter(merchantStock -> merchantStock.getProductID().equals(productId) && merchantStock.getMerchantID().equals(merchantId)).findFirst().orElse(null);//Return null if no productID Or merchantID  found
    }

    public void reduceStock(Integer productId, Integer merchantId){
        MerchantStock merchantStock=getMerchantStockProductIdMerchantId(productId,merchantId);
        if (merchantStock != null && merchantStock.getStock()>0)
            merchantStock.setStock(merchantStock.getStock()-1);
    }


    public void addMerchantStock(MerchantStock merchantStock){
        merchantStocks.add(merchantStock);
    }

    public boolean isUpdated(Integer id,MerchantStock merchantStock){
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)){
                merchantStocks.set(i,merchantStock);
                return true;
            }
        }return false;
    }


    public boolean isDelete(Integer id){
        for (int i = 0; i < merchantStocks.size(); i++) {
            if(merchantStocks.get(i).getId().equals(id)){
                merchantStocks.remove(i);
                return true;
            }
        }return false;
    }


    public boolean isAddMoreStocks(MerchantStock merchantStock){
        Integer productID= merchantStock.getProductID();
        Integer merchantID= merchantStock.getMerchantID();
        Integer amount= merchantStock.getStock();

        // Filter the list of merchant stocks to find a match based on productID and merchantID
        Optional<MerchantStock> matchStock =merchantStocks.stream().filter(stock ->stock.getProductID().equals(productID) &&
                stock.getMerchantID().equals(merchantID)).findFirst();

        if (matchStock.isPresent()){
            // If a match stock is found, update the stock by adding amount
            matchStock.get().setStock(matchStock.get().getStock()+amount);
            return true;
        }
        return false;
    }

    public MerchantStock searchID(Integer id){
        for (MerchantStock merchantStock:merchantStocks) {
            if (merchantStock.getMerchantID().equals(id)){
                return merchantStock;
            }
        }
        return null;
    }

    public ArrayList<MerchantStock> sortMerchantStock(){
        ArrayList<MerchantStock> sortedMerchantStock=new ArrayList<>();
        for (MerchantStock merchantStock:merchantStocks) {
            sortedMerchantStock.add(merchantStock);
        }
        Comparator<MerchantStock> comparatorName=Comparator.comparing(MerchantStock::getMerchantID);
        Collections.sort(sortedMerchantStock,comparatorName);
        return sortedMerchantStock;
    }
}
