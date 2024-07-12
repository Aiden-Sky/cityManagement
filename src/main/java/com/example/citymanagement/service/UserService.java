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

    public boolean addUserInfo(String account, String name, int age) {
        User user = new User();
        user.setaccount(account);
        user.setUserName(name);
        int res = userMapper.insertUserInfo(user);
        if (res > 0) return true;  //上传成功
        else return false;   //上传失败
    }

    public boolean Search(String ID, String name, int age) {
        //Todo


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

            if (!"SystemAdmin".equals(checkUser.getUserType())) {
                return null;
            }

            String hashedPassword = hashPassword(password);
            if (checkUser.getPasswordHash().equals(hashedPassword)) {
                return jwtUtil.generateToken(account);
            }
            return null;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //哈希函数
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
