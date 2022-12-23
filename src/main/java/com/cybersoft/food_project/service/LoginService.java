package com.cybersoft.food_project.service;


import com.cybersoft.food_project.entity.UserEntity;

public interface LoginService {
    boolean checkLogin(String email, String password);
    UserEntity checkLogin(String email);

}
