package com.example.citymanagement.service;

import com.example.citymanagement.Dao.UserInfomMapper;
import com.example.citymanagement.entity.Manager;
import com.example.citymanagement.entity.Resident;
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

    /**
     * 通过居民账户名查找对应的ID
     * 
     * @param account 居民账户名
     * @return 对应的居民ID，如果未找到则返回null
     */
    public Long findResidentIdByAccount(String account) {
        return userInfomMapper.findResidentIdByAccount(account);
    }

    /**
     * 通过居民账户名查找对应的居民信息
     * 
     * @param account 居民账户名
     * @return 居民信息对象，如果未找到则返回null
     */
    public Resident findResidentByAccount(String account) {
        return userInfomMapper.findResidentByAccount(account);
    }
}
