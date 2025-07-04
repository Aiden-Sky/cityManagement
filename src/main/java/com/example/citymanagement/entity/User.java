package com.example.citymanagement.entity;

public class User {

    private String account;         // 用户ID
    private String passwordHash;
    private String userType;
    private String email;
    private String phoneNumber;
    private int isActive;       // 是否启用


    // Getter和Setter方法
    public String getaccount() {
        return account;
    }

    public void setaccount(String account) {
        this.account = account;
    }



    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    // toString方法

}
