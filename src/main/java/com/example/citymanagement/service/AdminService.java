package com.example.citymanagement.service;

import com.example.citymanagement.Dao.AdminMapper;
import com.example.citymanagement.Dao.UserMapper;
import com.example.citymanagement.Dto.AdminInfoDTO;
import com.example.citymanagement.Dto.PasswordChangeDTO;
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

    /**
     * 获取管理员信息DTO，合并Admin和User信息
     * 
     * @param account 账户名
     * @return 管理员信息DTO
     */
    public AdminInfoDTO getAdminInfoDTO(String account) {
        // 获取管理员基本信息
        Admin admin = adminMapper.getAdminByAccount(account);
        if (admin == null) {
            return null;
        }

        // 获取用户信息
        User user = userMapper.getUserByAccount(account);
        if (user == null) {
            return null;
        }

        // 合并信息到DTO
        AdminInfoDTO adminInfoDTO = new AdminInfoDTO();
        adminInfoDTO.setAccount(admin.getAccount());
        adminInfoDTO.setName(admin.getName());
        adminInfoDTO.setSex(admin.isSex());
        adminInfoDTO.setPosition(admin.getPosition());
        adminInfoDTO.setRemark(admin.getRemark());
        // 从User表获取信息
        adminInfoDTO.setUserType(user.getUserType());
        adminInfoDTO.setEmail(user.getEmail());
        adminInfoDTO.setPhoneNumber(user.getPhoneNumber());
        adminInfoDTO.setIsActive(user.getIsActive());

        return adminInfoDTO;
    }

    /**
     * 更新管理员信息
     * 
     * @param adminInfoDTO 管理员信息DTO
     * @return 是否成功
     */
    @Transactional
    public boolean updateAdminInfo(AdminInfoDTO adminInfoDTO) {
        try {
            // 更新Admin表
            Admin admin = new Admin();
            admin.setAccount(adminInfoDTO.getAccount());
            admin.setName(adminInfoDTO.getName());
            admin.setSex(adminInfoDTO.getSex());
            admin.setPosition(adminInfoDTO.getPosition());
            admin.setRemark(adminInfoDTO.getRemark());

            // 更新User表
            User user = new User();
            user.setaccount(adminInfoDTO.getAccount());
            user.setUserType(adminInfoDTO.getUserType());
            user.setEmail(adminInfoDTO.getEmail());
            user.setPhoneNumber(adminInfoDTO.getPhoneNumber());
            user.setIsActive(adminInfoDTO.getIsActive());

            // 执行更新
            int adminUpdateResult = adminMapper.updateAdmin(admin);
            int userUpdateResult = userMapper.updateUserInfo(user);

            return adminUpdateResult > 0 && userUpdateResult > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改密码
     * 
     * @param account         账户名
     * @param currentPassword 当前密码
     * @param newPassword     新密码
     * @return 是否成功
     */
    @Transactional
    public boolean changePassword(String account, String currentPassword, String newPassword) {
        try {
            // 验证当前密码
            User user = userMapper.getUserByAccount(account);
            if (user == null || !user.getPasswordHash().equals(hashPassword(currentPassword))) {
                return false;
            }

            // 更新密码
            user.setPasswordHash(hashPassword(newPassword));
            return userMapper.updateUser(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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