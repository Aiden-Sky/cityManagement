package com.example.citymanagement.service;

import com.example.citymanagement.Dao.ResidentMapper;
import com.example.citymanagement.Dao.UserMapper;
import com.example.citymanagement.Dto.ResidentRegisterDTO;
import com.example.citymanagement.entity.Resident;
import com.example.citymanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class ResidentService {
    @Autowired(required = false)
    private ResidentMapper residentMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    // 获取所有居民信息
    public List<Resident> getAllResidents() {
        return residentMapper.getAllResidents();
    }

    // 根据账号获取居民信息
    public Resident getResidentByAccount(String account) {
        return residentMapper.getResidentByAccount(account);
    }

    // 根据ID获取居民信息
    public Resident getResidentById(Long id) {
        return residentMapper.getResidentById(id);
    }

    // 添加新居民（同时添加用户信息和居民信息）
    @Transactional
    public boolean registResident(ResidentRegisterDTO residentRegisterDTO) {
        try {
            // 创建用户信息
            User user = new User();
            user.setaccount(residentRegisterDTO.getAccount());
            user.setPasswordHash(hashPassword(residentRegisterDTO.getPassword()));
            user.setUserType("Resident");
            user.setEmail(residentRegisterDTO.getEmail());
            user.setPhoneNumber(residentRegisterDTO.getPhoneNumber());
            user.setIsActive(1);

            // 插入用户信息
            int userInsert = userMapper.insertUser(user);

            // 创建居民信息
            Resident resident = new Resident();
            resident.setAccount(residentRegisterDTO.getAccount());
            resident.setIdNumber(residentRegisterDTO.getIdNumber());
            resident.setEmail(residentRegisterDTO.getEmail());
            resident.setPhoneNumber(residentRegisterDTO.getPhoneNumber());
            resident.setIsActive("1");

            // 插入居民信息
            int residentInsert = residentMapper.insertResident(resident);

            return userInsert > 0 && residentInsert > 0;


        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新居民信息
    @Transactional
    public boolean updateResident(Resident resident) {
        try {
            // 更新用户基本信息
            User user = new User();
            user.setaccount(resident.getAccount());
            user.setEmail(resident.getEmail());
            user.setPhoneNumber(resident.getPhoneNumber());

            // 如果提供了新密码，则更新密码
            if (resident.getPasswordHash() != null && !resident.getPasswordHash().isEmpty()) {
                user.setPasswordHash(hashPassword(resident.getPasswordHash()));
            }

            // 更新用户和居民信息
            int userUpdate = userMapper.updateUser(user);
            int residentUpdate = residentMapper.updateResident(resident);

            return userUpdate > 0 && residentUpdate > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除居民信息
    @Transactional
    public boolean deleteResident(String account) {
        try {
            // 先删除居民信息，再删除用户信息
            int residentDelete = residentMapper.deleteResident(account);
            int userDelete = userMapper.deleteUser(account);

            return residentDelete > 0 && userDelete > 0;
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