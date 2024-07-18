package com.example.citymanagement.entity;

public class User {

    private String account;         // 用户ID
    private String userName;    // 用户名
    private String passwordHash;
    private String userType;
    private String email;
    private String phoneNumber;
    private int isActive;       // 是否活跃

    // 构造函数
    public User(String passwordHash, String userType, int isActive) {
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.isActive = isActive;
    }
    public User() {}

    public User(String account, String userName, String passwordHash, String userType, int isActive) {
        this.account = account;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.isActive = isActive;
    }

    // Getter和Setter方法
    public String getaccount() {
        return account;
    }

    public void setaccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    @Override
    public String toString() {
        return "User{" +
                "account=" + account +
                ", userName='" + userName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
