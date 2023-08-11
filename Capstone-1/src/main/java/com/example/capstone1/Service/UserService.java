package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
public class UserService {

    ArrayList<User> users=new ArrayList<>();

    public ArrayList<User> getAllUser(){
        return users;
    }

    public User getUserById(Integer userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null; // Return null if no user with the given ID is found
    }

    public void addUser(User user){
        users.add(user);
    }

    public boolean isUpdated(Integer id,User user){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)){
                users.set(i,user);
                return true;
            }
        }return false;
    }

    public boolean isDeleted(Integer id){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)){
                users.remove(i);
                return true;
            }
        }return false;
    }


    public User searchName(String name){
        for (User user:users) {
            if (user.getUserName().equalsIgnoreCase(name)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> sortUser(){
        ArrayList<User> sortedUser=new ArrayList<>();
        for (User user:users) {
            sortedUser.add(user);
        }
        Comparator<User> comparatorName=Comparator.comparing(User::getUserName);
        Collections.sort(sortedUser,comparatorName);
        return sortedUser;
    }
}
