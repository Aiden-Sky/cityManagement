package com.example.citymanagement.Dto;

/**
 * MFA验证DTO，用于接收验证码
 */
public class MfaVerifyDTO {
    private String code;

    public MfaVerifyDTO() {
    }

    public MfaVerifyDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}