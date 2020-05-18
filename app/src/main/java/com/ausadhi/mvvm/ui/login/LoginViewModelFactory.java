package com.ausadhi.mvvm.ui.login;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.ausadhi.mvvm.data.network.services.LoginService;

public class LoginViewModelFactory implements ViewModelProvider.Factory  {

    LoginService loginService;
    Context mContext;

    public LoginViewModelFactory(LoginService loginService,Context mContext) {
        this.loginService = loginService;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(loginService,mContext);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");    }
}
