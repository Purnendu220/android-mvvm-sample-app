package com.ausadhi.mvvm.data.network.model;

public class UserModel {
    String userId;
    String name;
    String userMobile;
    String userEmail;
    String userPassword;
    String userType;
    String firebaseToken;

    public UserModel(String userId, String name, String userMobile, String userEmail, String userPassword,String userType,String firebaseToken) {
        this.userId = userId;
        this.name = name;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
        this.firebaseToken = firebaseToken;
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

    public String getUserType() {
        return userType;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
