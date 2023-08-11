package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants=new ArrayList<>();

    public ArrayList<Merchant> getAllMerchant(){
        return merchants;
    }

    public void addMerchants(Merchant merchant){
        merchants.add(merchant);
    }


    public boolean updateMerchant(Integer id, Merchant merchant){
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)){
                merchants.set(i,merchant);
                return true;
            }
        }return false;
    }

    public boolean isDeleted(Integer id){
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)){
                merchants.remove(i);
                return true;
            }
        }return false;
    }


    public Merchant searchName(String name){
        for (Merchant merchant:merchants) {
            if (merchant.getName().equalsIgnoreCase(name)){
                return merchant;
            }
        }
        return null;
    }

    public ArrayList<Merchant> sortMerchant(){
        ArrayList<Merchant> sortedMerchant=new ArrayList<>();
        for (Merchant merchant:merchants) {
            sortedMerchant.add(merchant);
        }
        Comparator<Merchant> comparatorName=Comparator.comparing(Merchant::getName);
        Collections.sort(sortedMerchant,comparatorName);
        return sortedMerchant;
    }
}
