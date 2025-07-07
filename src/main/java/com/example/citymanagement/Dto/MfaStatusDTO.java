package com.example.citymanagement.Dto;

/**
 * MFA状态响应DTO
 */
public class MfaStatusDTO {
    private boolean enabled;

    public MfaStatusDTO() {
    }

    public MfaStatusDTO(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}