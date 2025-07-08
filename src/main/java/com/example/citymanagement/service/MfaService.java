package com.example.citymanagement.service;

import com.example.citymanagement.Dao.MfaConfigMapper;
import com.example.citymanagement.Dto.MfaSetupDTO;
import com.example.citymanagement.Dto.MfaStatusDTO;
import com.example.citymanagement.entity.MfaConfig;
import com.example.citymanagement.util.TotpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * MFA服务类
 */
@Service
public class MfaService {

    @Autowired(required = false)
    private MfaConfigMapper mfaConfigMapper;

    @Autowired
    private TotpUtil totpUtil;

    @Value("${app.name:城市管理系统}")
    private String appName;

    /**
     * 获取MFA状态
     *
     * @param account 用户账号
     * @return MFA状态DTO
     */
    public MfaStatusDTO getMfaStatus(String account) {
        MfaConfig mfaConfig = mfaConfigMapper.getMfaConfigByAccount(account);
        boolean enabled = mfaConfig != null && mfaConfig.isEnabled();
        return new MfaStatusDTO(enabled);
    }

    /**
     * 设置MFA
     *
     * @param account 用户账号
     * @return MFA设置DTO
     */
    public MfaSetupDTO setupMfa(String account) {
        // 查找现有配置
        MfaConfig existingConfig = mfaConfigMapper.getMfaConfigByAccount(account);
        String secretKey;

        if (existingConfig != null && existingConfig.getSecretKey() != null
                && !existingConfig.getSecretKey().isEmpty()) {
            // 使用现有密钥
            secretKey = existingConfig.getSecretKey();
        } else {
            // 生成新密钥
            secretKey = totpUtil.generateSecretKey();

            // 保存新配置
            MfaConfig mfaConfig = new MfaConfig();
            mfaConfig.setAccount(account);
            mfaConfig.setSecretKey(secretKey);
            mfaConfig.setEnabled(false);
            mfaConfig.setCreatedTime(new Date());
            mfaConfig.setUpdatedTime(new Date());

            if (existingConfig == null) {
                mfaConfigMapper.insertMfaConfig(mfaConfig);
            } else {
                mfaConfigMapper.updateMfaConfig(mfaConfig);
            }
        }

        // 生成二维码URL
        String qrCodeUrl = totpUtil.generateQrCodeUrl(appName, account, secretKey);

        // 返回设置DTO
        return new MfaSetupDTO(qrCodeUrl, secretKey);
    }

    /**
     * 验证并启用MFA
     *
     * @param account 用户账号
     * @param code    验证码
     * @return 是否验证成功
     */
    public boolean verifyAndEnableMfa(String account, String code) {
        MfaConfig mfaConfig = mfaConfigMapper.getMfaConfigByAccount(account);

        if (mfaConfig == null || mfaConfig.getSecretKey() == null) {
            return false;
        }

        // 验证验证码
        boolean verified = totpUtil.verifyCode(mfaConfig.getSecretKey(), code);

        if (verified) {
            // 启用MFA
            mfaConfigMapper.updateMfaStatus(account, true);
        }

        return verified;
    }

    /**
     * 禁用MFA
     *
     * @param account 用户账号
     * @return 是否成功
     */
    public boolean disableMfa(String account) {
        MfaConfig mfaConfig = mfaConfigMapper.getMfaConfigByAccount(account);

        if (mfaConfig == null) {
            return false;
        }

        // 禁用MFA
        mfaConfigMapper.updateMfaStatus(account, false);
        return true;
    }

    /**
     * 验证MFA验证码
     *
     * @param account 用户账号
     * @param code    验证码
     * @return 是否验证成功
     */
    public boolean verifyMfaCode(String account, String code) {
        MfaConfig mfaConfig = mfaConfigMapper.getMfaConfigByAccount(account);

        if (mfaConfig == null || !mfaConfig.isEnabled() || mfaConfig.getSecretKey() == null) {
            return false;
        }

        // 验证验证码
        return totpUtil.verifyCode(mfaConfig.getSecretKey(), code);
    }

    /**
     * 检查用户是否启用了MFA
     *
     * @param account 用户账号
     * @return 是否启用MFA
     */
    public boolean isMfaEnabled(String account) {
        MfaConfig mfaConfig = mfaConfigMapper.getMfaConfigByAccount(account);
        return mfaConfig != null && mfaConfig.isEnabled();
    }
}