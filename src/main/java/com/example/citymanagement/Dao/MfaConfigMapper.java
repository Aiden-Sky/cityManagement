package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.MfaConfig;

/**
 * MFA配置Mapper接口
 */
public interface MfaConfigMapper {

    /**
     * 通过账户名获取MFA配置
     * 
     * @param account 账户名
     * @return MFA配置
     */
    MfaConfig getMfaConfigByAccount(String account);

    /**
     * 添加MFA配置
     * 
     * @param mfaConfig MFA配置
     * @return 影响行数
     */
    int insertMfaConfig(MfaConfig mfaConfig);

    /**
     * 更新MFA配置
     * 
     * @param mfaConfig MFA配置
     * @return 影响行数
     */
    int updateMfaConfig(MfaConfig mfaConfig);

    /**
     * 删除MFA配置
     * 
     * @param account 账户名
     * @return 影响行数
     */
    int deleteMfaConfig(String account);
}