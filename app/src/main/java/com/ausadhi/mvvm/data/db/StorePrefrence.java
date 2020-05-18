package com.ausadhi.mvvm.data.db;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.preference.PowerPreference;
import com.preference.Preference;

public class StorePrefrence {

    private static StorePrefrence sInstance;
    private final Preference prefrence;

    private StorePrefrence() {
       prefrence = PowerPreference.getDefaultFile();
    }

    public static StorePrefrence getInstance() {
        if (sInstance == null) {
            sInstance = new StorePrefrence();
        }
        return sInstance;
    }

    public void setUser(UserModel user){
        prefrence.putObject(ApiConstants.Prefrences.USER_KEY,user);
    }
    public UserModel getUser(){
        return prefrence.getObject(ApiConstants.Prefrences.USER_KEY,UserModel.class,null);
    }


}
