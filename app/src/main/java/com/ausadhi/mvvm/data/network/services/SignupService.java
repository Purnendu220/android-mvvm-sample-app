package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupService {
    private static SignupService instance;
    private final CollectionReference mUserDatabase;
    public DatabaseReference mSignupDatabase;
    public FirebaseAuth userAuthRefrence;

    public static SignupService getInstance() {
        if (instance == null) {
            instance = new SignupService();
        }
        return instance;
    }
    private SignupService() {
       mSignupDatabase  = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.USER_REFRENCE);
       userAuthRefrence = FirebaseAuth.getInstance();
       mUserDatabase =  FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.USER_REFRENCE);
    }
    public DatabaseReference getSighUpRefrence(){
        return mSignupDatabase;
    }

    public CollectionReference getDatabasepRefrence(){
        return mUserDatabase;
    }
    public FirebaseAuth getauthRefrence(){
        return userAuthRefrence;
    }

}
