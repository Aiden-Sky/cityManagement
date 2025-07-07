package com.example.citymanagement.Dto;

/**
 * MFA设置响应DTO
 */
public class MfaSetupDTO {
    private String qrCodeUrl;
    private String secretKey;

    public MfaSetupDTO() {
    }

    public MfaSetupDTO(String qrCodeUrl, String secretKey) {
        this.qrCodeUrl = qrCodeUrl;
        this.secretKey = secretKey;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}