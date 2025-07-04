package com.example.citymanagement.Dto;

public class ResidentRegisterDTO {
    private String account;
    private String password;
    private String phoneNumber;
    private String email;
    private String idNumber;

    // 可选字段：根据需要添加
    // private String name;
    // private Boolean sex;
    // private Date dateOfBirthday;
    // private String address;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

}
