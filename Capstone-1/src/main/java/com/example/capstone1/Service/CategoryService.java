package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import com.example.capstone1.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
public class CategoryService {
    ArrayList<Category> categories=new ArrayList<>();

    public ArrayList<Category> getAllCategory(){
        return categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public boolean isUpdated(Integer id,Category category){
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)){
                categories.set(i,category);
                return true;
            }
        }
        return false;
    }
    public boolean isDeleted(Integer id){
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)){
                categories.remove(i);
                return true;
            }
        }return false;
    }

    public Category searchName(String name){
        for (Category category:categories) {
            if (category.getName().equalsIgnoreCase(name)){
                return category;
            }
        }
        return null;
    }

    public ArrayList<Category> sortCategory(){
        ArrayList<Category> sortedCategory=new ArrayList<>();
        for (Category catecory:categories) {
            sortedCategory.add(catecory);
        }
        Comparator<Category> comparatorName=Comparator.comparing(Category::getName);
        Collections.sort(sortedCategory,comparatorName);
        return sortedCategory;
    }

}
