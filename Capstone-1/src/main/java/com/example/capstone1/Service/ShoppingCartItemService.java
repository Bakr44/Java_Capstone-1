package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.ShoppingCartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShoppingCartItemService {
    private ArrayList<ShoppingCartItem> items = new ArrayList<>();

    public void addItem(Product product, Integer quantity) {
        // Check if the item already exists in the cart
        for (ShoppingCartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // If not, add a new ShoppingCartItem
        items.add(new ShoppingCartItem(product, quantity));
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().getId().equals(product.getId()));
    }

    public Integer calculateTotalCost() {
        Integer totalCost = 0;
        for (ShoppingCartItem item : items) {
            totalCost += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    public ArrayList<ShoppingCartItem> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
    }

    public boolean isCartEmpty(){
        return items.isEmpty();
    }
}
