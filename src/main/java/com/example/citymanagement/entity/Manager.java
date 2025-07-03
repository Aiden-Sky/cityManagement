package com.example.citymanagement.entity;

public class Manager {

    private int manageID;
    private String account;
    private String name;
    private boolean sax;
    private String address;
    private String idNumber;
    private String department;
    private String position;

    // Getters and setters

    public int getManageID() {
        return manageID;
    }

    public void setManageID(int manageID) {
        this.manageID = manageID;
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

    public boolean isSax() {
        return sax;
    }

    public void setSax(boolean sax) {
        this.sax = sax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
