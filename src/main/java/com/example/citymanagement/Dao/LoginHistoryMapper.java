package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.LoginHistory;

import java.util.List;

/**
 * 登录历史Mapper接口
 */
public interface LoginHistoryMapper {

    /**
     * 添加登录记录
     * 
     * @param loginHistory 登录记录
     * @return 影响行数
     */
    int insertLoginHistory(LoginHistory loginHistory);

    /**
     * 获取指定账户的登录历史
     * 
     * @param account 账户名
     * @param limit   限制条数
     * @return 登录历史列表
     */
    List<LoginHistory> getLoginHistoryByAccount(String account, int limit);
}