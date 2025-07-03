package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.Admin;
import java.util.List;

public interface AdminMapper {
    // 根据账户查询管理员信息
    Admin getAdminByAccount(String account);

    // 查询所有管理员
    List<Admin> getAllAdmins();

    // 添加新管理员
    int insertAdmin(Admin admin);

    // 更新管理员信息
    int updateAdmin(Admin admin);

    // 删除管理员信息
    int deleteAdmin(String account);

    // 根据管理员ID查询信息
    Admin getAdminById(int adminId);
}