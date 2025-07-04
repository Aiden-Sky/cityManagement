package com.example.citymanagement.service;

import com.example.citymanagement.Dao.UserMapper;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    public boolean setUser(User user) {
        try {
            // 如果用户提供了密码，则对密码进行哈希处理
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                user.setPasswordHash(hashPassword(user.getPasswordHash()));
            }

            // 根据用户的账户信息决定插入或更新
            User existingUser = userMapper.login(user.getaccount());
            int res;
            if (existingUser == null) {
                // 如果用户不存在，插入新用户
                res = userMapper.insertUser(user);
            } else {
                // 如果用户存在，更新现有用户
                res = userMapper.updateUser(user);
            }
            return res > 0; // 如果操作成功，则返回true，否则返回false
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据账号获取用户信息
    public User getUserByAccount(String account) {
        try {
            return userMapper.login(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean Search(String ID, String name, int age) {
        // Todo

        return false;
    }

    public String authenticate(String account, String password) {
        if (account == null || account.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        try {
            User checkUser = userMapper.login(account);

            if (checkUser == null) {
                return null;
            }

            String hashedPassword = hashPassword(password);
            if (!checkUser.getPasswordHash().equals(hashedPassword)) {
                return null;
            }

            // 根据用户类型生成token
            return jwtUtil.generateToken(account, checkUser.getUserType());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 哈希函数
    private String hashPassword(String password) throws NoSuchAlgorithmException {
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
    }

    public boolean checkManage(String account) {
        User user = userMapper.login(account);
        if (user != null && "Management".equals(user.getUserType())) {
            return true;
        }
        return false;
    }
}
