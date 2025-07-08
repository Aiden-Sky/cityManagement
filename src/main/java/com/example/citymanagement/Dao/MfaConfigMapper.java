package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.MfaConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MfaConfigMapper {
    /**
     * 根据账号获取MFA配置
     * 
     * @param account 账号
     * @return MFA配置
     */
    MfaConfig getMfaConfigByAccount(String account);

    /**
     * 创建MFA配置
     * 
     * @param mfaConfig MFA配置
     * @return 影响的行数
     */
    int insertMfaConfig(MfaConfig mfaConfig);

    /**
     * 更新MFA配置
     * 
     * @param mfaConfig MFA配置
     * @return 影响的行数
     */
    int updateMfaConfig(MfaConfig mfaConfig);

    /**
     * 更新MFA状态
     * 
     * @param account 账号
     * @param enabled 是否启用
     * @return 影响的行数
     */
    int updateMfaStatus(@Param("account") String account, @Param("enabled") boolean enabled);

    /**
     * 删除MFA配置
     * 
     * @param account 账号
     * @return 影响的行数
     */
    int deleteMfaConfig(String account);
}