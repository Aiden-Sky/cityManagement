package com.example.citymanagement.service;

import com.example.citymanagement.Dao.UserMapper;
import com.example.citymanagement.entity.Manager;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private com.example.citymanagement.Dao.ManagerMapper managerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * 获取所有城市管理者
     */
    public List<Manager> getAllManagers() {
        return managerMapper.getAllManagers();
    }

    /**
     * 通过ID获取城市管理者
     */
    public Manager getManagerById(int manageId) {
        return managerMapper.getManagerById(manageId);
    }

    /**
     * 通过账号获取城市管理者
     */
    public Manager getManagerByAccount(String account) {
        return managerMapper.getManagerByAccount(account);
    }

    /**
     * 添加城市管理者
     */
    @Transactional
    public boolean addManager(Manager manager) {
        return addManagerWithPassword(manager, "123456"); // 使用默认密码
    }

    /**
     * 添加城市管理者(带密码)
     */
    @Transactional
    public boolean addManagerWithPassword(Manager manager, String password) {
        try {
            // 首先添加用户信息
            User user = new User();
            user.setaccount(manager.getAccount());
            user.setPasswordHash(passwordUtil.hashPassword(password));
            user.setUserType("Management");
            user.setIsActive(1); // 假设1表示激活
            userMapper.insertUser(user);

            // 添加城市管理者信息
            return managerMapper.insertManager(manager) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新城市管理者信息
     */
    @Transactional
    public boolean updateManager(Manager manager) {
        try {
            // 如果需要更新密码，在Controller中处理
            User user = userMapper.getUserByAccount(manager.getAccount());
            if (user != null) {
                // 更新是否激活状态，这里假设我们从外部获取了isActive状态
                // 实际中应该从请求中获取
                user.setIsActive(1); // 默认设置为激活状态
                userMapper.updateUser(user);
            }

            // 更新城市管理者信息
            return managerMapper.updateManager(manager) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新城市管理者信息并更新密码
     */
    @Transactional
    public boolean updateManagerWithPassword(Manager manager, String newPassword) {
        try {
            // 更新用户密码
            User user = userMapper.getUserByAccount(manager.getAccount());
            if (user != null) {
                user.setPasswordHash(passwordUtil.hashPassword(newPassword));
                user.setIsActive(1); // 默认设置为激活状态
                userMapper.updateUser(user);
            }

            // 更新城市管理者信息
            return managerMapper.updateManager(manager) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除城市管理者
     */
    @Transactional
    public boolean deleteManager(int manageId) {
        try {
            // 获取城市管理者信息
            Manager manager = managerMapper.getManagerById(manageId);
            if (manager == null) {
                return false;
            }

            // 首先删除城市管理者信息
            int result = managerMapper.deleteManager(manageId);

            // 然后删除对应的用户信息
            if (result > 0) {
                userMapper.deleteUser(manager.getAccount());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据部门获取城市管理者列表
     */
    public List<Manager> getManagersByDepartment(String department) {
        return managerMapper.getManagersByDepartment(department);
    }

    /**
     * 重置城市管理者密码
     */
    @Transactional
    public boolean resetManagerPassword(String account, String newPassword) {
        try {
            User user = userMapper.getUserByAccount(account);
            if (user != null && "Management".equals(user.getUserType())) {
                user.setPasswordHash(passwordUtil.hashPassword(newPassword));
                return userMapper.updateUser(user) > 0;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}