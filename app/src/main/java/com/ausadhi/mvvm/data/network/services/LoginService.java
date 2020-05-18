package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.ausadhi.mvvm.data.network.model.LoginResponse;
import com.ausadhi.mvvm.data.network.model.MovieResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class LoginService {
    private static LoginService instance;
    public DatabaseReference mLoginDatabase;
    public FirebaseAuth userAuthRefrence;

    public static LoginService getInstance() {
        if (instance == null) {
            instance = new LoginService();
        }
        return instance;
    }

    private LoginService() {
        mLoginDatabase  = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.USER_REFRENCE);
        userAuthRefrence = FirebaseAuth.getInstance();
    }

    public DatabaseReference getDatabaseRefrence(){
        return mLoginDatabase;
    }
    public FirebaseAuth getauthRefrence(){
        return userAuthRefrence;
    }


}
