package com.example.citymanagement.service;


import com.example.citymanagement.Dao.UserInfomMapper;
import com.example.citymanagement.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfomService {
    @Autowired(required = false)
    private UserInfomMapper userInfomMapper;

    public List<Manager> getAllManagers() {
        return userInfomMapper.getAllManage();
    }
}
