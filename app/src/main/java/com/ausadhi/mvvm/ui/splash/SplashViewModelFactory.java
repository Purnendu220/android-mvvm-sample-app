package com.ausadhi.mvvm.ui.splash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    Context mContext;

    public SplashViewModelFactory(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(mContext);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");    }
}


