package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.Manager;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ManagerMapper {

    /**
     * 获取所有城市管理者
     */
    List<Manager> getAllManagers();

    /**
     * 通过ID获取城市管理者
     */
    Manager getManagerById(int manageId);

    /**
     * 通过账号获取城市管理者
     */
    Manager getManagerByAccount(String account);

    /**
     * 添加城市管理者
     */
    int insertManager(Manager manager);

    /**
     * 更新城市管理者信息
     */
    int updateManager(Manager manager);

    /**
     * 删除城市管理者
     */
    int deleteManager(int manageId);

    /**
     * 根据部门获取城市管理者列表
     */
    List<Manager> getManagersByDepartment(String department);
}