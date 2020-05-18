package com.ausadhi.mvvm.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @Expose
    @SerializedName("users")
    private List<UserData> users;

    public List<UserData> getUsers() {
        return users;
    }
}
