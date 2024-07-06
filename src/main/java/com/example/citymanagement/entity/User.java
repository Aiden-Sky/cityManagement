package com.example.citymanagement.entity;

public class User {

    private int    userID;     //用户ID
    private String userName;   //用户名
    private int    userAge;    //用户年龄

    public User() {
    }

    public User(int userID, String userName, int userAge) {
        this.userID = userID;
        this.userName = userName;
        this.userAge = userAge;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", userAge=" + userAge +
                '}';
    }
}
