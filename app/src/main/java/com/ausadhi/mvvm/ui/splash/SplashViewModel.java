package com.ausadhi.mvvm.ui.splash;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.ausadhi.mvvm.ui.base.BaseViewModel;

public class SplashViewModel extends BaseViewModel {

    private Context mContext;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    SplashViewModel(Context mContext) {
        this.mContext = mContext;
    }

    void startTimer(){
        isLoading.setValue(true);
        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {
                isLoading.setValue(false);
            }

        }, 5*1000);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
