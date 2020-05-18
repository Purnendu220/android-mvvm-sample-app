package com.ausadhi.mvvm.data.network.model;

public class UserModel {
    String userId;
    String name;
    String userMobile;
    String userEmail;
    String userPassword;

    public UserModel(String userId, String name, String userMobile, String userEmail, String userPassword) {
        this.userId = userId;
        this.name = name;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
