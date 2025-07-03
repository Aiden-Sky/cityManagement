package com.example.citymanagement.service;

import com.example.citymanagement.Dao.AdminMapper;
import com.example.citymanagement.Dao.UserMapper;
import com.example.citymanagement.entity.Admin;
import com.example.citymanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AdminService {
    @Autowired(required = false)
    private AdminMapper adminMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    // 获取所有管理员
    public List<Admin> getAllAdmins() {
        return adminMapper.getAllAdmins();
    }

    // 根据账号获取管理员
    public Admin getAdminByAccount(String account) {
        return adminMapper.getAdminByAccount(account);
    }

    // 根据ID获取管理员
    public Admin getAdminById(int id) {
        return adminMapper.getAdminById(id);
    }

    // 添加新管理员（同时添加用户和管理员信息）
    @Transactional
    public boolean addAdmin(Admin admin) {
        try {
            // 创建用户信息
            User user = new User();
            user.setaccount(admin.getAccount());
            user.setPasswordHash(hashPassword(admin.getRemark())); // 使用备注作为初始密码
            user.setUserType("SystemAdmin");
            user.setEmail("");
            user.setPhoneNumber("");
            user.setIsActive(1);

            // 插入用户信息
            int userInsert = userMapper.insertUser(user);

            // 插入管理员信息
            int adminInsert = adminMapper.insertAdmin(admin);

            return userInsert > 0 && adminInsert > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("添加管理员失败", e);
        }
    }

    // 更新管理员信息
    @Transactional
    public boolean updateAdmin(Admin admin) {
        try {
            // 更新用户和管理员信息
            int adminUpdate = adminMapper.updateAdmin(admin);
            return adminUpdate > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 重置管理员密码
    @Transactional
    public boolean resetAdminPassword(String account, String newPassword) {
        try {
            User user = new User();
            user.setaccount(account);
            user.setPasswordHash(hashPassword(newPassword));

            int userUpdate = userMapper.updateUser(user);
            return userUpdate > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除管理员信息
    @Transactional
    public boolean deleteAdmin(String account) {
        try {
            // 先删除管理员信息，再删除用户信息
            int adminDelete = adminMapper.deleteAdmin(account);
            int userDelete = userMapper.deleteUser(account);

            return adminDelete > 0 && userDelete > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 密码哈希
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("密码哈希失败", e);
        }
    }
}