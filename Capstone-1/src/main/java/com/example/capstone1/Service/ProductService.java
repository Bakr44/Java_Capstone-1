package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
public class ProductService {

    ArrayList<Product> products=new ArrayList<>();

    public ArrayList<Product> getAllProduct(){
        return products;
    }




    public void addProduct(Product product){
        products.add(product);
    }

    public boolean updateProduct(Integer id,Product product){
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)){
                products.set(i,product);
                return true;
            }
        }return false;
    }

    public boolean isDeleted(Integer id){
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)){
                products.remove(i);
                return true;
            }
        }return false;
    }

    public Product searchName(String name){
        for (Product product:products) {
            if (product.getName().equalsIgnoreCase(name)){
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> sortProduct(){
        ArrayList<Product> sortedProduct=new ArrayList<>();
        for (Product product:products) {
            sortedProduct.add(product);
        }
        Comparator<Product> comparatorName=Comparator.comparing(Product::getName);
        Collections.sort(sortedProduct,comparatorName);
        return sortedProduct;
    }

    public Product getProductById(Integer productId) {
        // Loop through Array of products or retrieve from database
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null; // Return null if product with given ID is not found
    }
}
