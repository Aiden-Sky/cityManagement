package com.example.citymanagement.entity;

public class Admin {
    private int adminID; // 主键

    private String account; // 账户，允许为空

    private String name; // 姓名，不允许为空

    private boolean sex; // 性别，使用布尔值表示（true=男，false=女）

    private String position; // 职位，不允许为空

    private String remark; // 备注，允许为空

    // Getters 和 Setters
    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
