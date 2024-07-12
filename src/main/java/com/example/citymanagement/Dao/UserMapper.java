package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.User;


public interface UserMapper {

    int insertUserInfo(User user);

    User login(String account);
}