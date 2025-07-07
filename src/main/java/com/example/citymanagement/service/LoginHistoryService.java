package com.example.citymanagement.service;

import com.example.citymanagement.Dao.LoginHistoryMapper;
import com.example.citymanagement.Dto.LoginHistoryDTO;
import com.example.citymanagement.entity.LoginHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 登录历史服务类
 */
@Service
public class LoginHistoryService {

    @Autowired(required = false)
    private LoginHistoryMapper loginHistoryMapper;

    /**
     * 记录登录信息
     * 
     * @param account 账户名
     * @param ip      IP地址
     * @param device  设备信息
     * @param status  状态(成功/失败)
     * @return 是否成功
     */
    public boolean recordLogin(String account, String ip, String device, String status) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setAccount(account);
        loginHistory.setLoginTime(new Date());
        loginHistory.setIp(ip);
        loginHistory.setDevice(device);
        loginHistory.setStatus(status);

        try {
            return loginHistoryMapper.insertLoginHistory(loginHistory) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取用户的登录历史
     * 
     * @param account 账户名
     * @param limit   返回记录数量
     * @return 登录历史列表
     */
    public List<LoginHistoryDTO> getLoginHistory(String account, int limit) {
        List<LoginHistoryDTO> dtos = new ArrayList<>();
        try {
            List<LoginHistory> histories = loginHistoryMapper.getLoginHistoryByAccount(account, limit);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (LoginHistory history : histories) {
                LoginHistoryDTO dto = new LoginHistoryDTO();
                dto.setAccount(history.getAccount());
                dto.setTime(sdf.format(history.getLoginTime()));
                dto.setIp(history.getIp());
                dto.setDevice(history.getDevice());
                dto.setStatus(history.getStatus());
                dtos.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtos;
    }
}