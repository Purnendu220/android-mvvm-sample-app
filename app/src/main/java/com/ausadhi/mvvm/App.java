package com.ausadhi.mvvm;

import android.app.Application;

import com.preference.PowerPreference;


/**
 * Created by Purnendu on 10/03/2020.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        PowerPreference.init(this);

    }

    public static App getInstance() {
        return sInstance;
    }

}
