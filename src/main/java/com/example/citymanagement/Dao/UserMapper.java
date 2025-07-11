package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.User;

public interface UserMapper {

    User login(String account);

    User getUserByAccount(String account);

    int insertUser(User user);

    int updateUser(User user);

    int updateUserInfo(User user);

    int deleteUser(String account);
}