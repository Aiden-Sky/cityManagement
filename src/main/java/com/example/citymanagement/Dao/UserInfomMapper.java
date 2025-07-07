package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.Manager;
import com.example.citymanagement.entity.Resident;

import java.util.List;

public interface UserInfomMapper {

    List<Manager> getAllManage();

    // 通过账户名查找居民ID
    Long findResidentIdByAccount(String account);

    // 通过账户名查找居民信息
    Resident findResidentByAccount(String account);
}