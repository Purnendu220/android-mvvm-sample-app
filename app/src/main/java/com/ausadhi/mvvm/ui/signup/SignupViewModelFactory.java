package com.ausadhi.mvvm.ui.signup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.data.network.services.SignupService;
import com.ausadhi.mvvm.ui.login.LoginViewModel;

public class SignupViewModelFactory implements ViewModelProvider.Factory  {

    SignupService signupService;
    Context mContext;

    public SignupViewModelFactory(SignupService signupService,Context mContext) {
        this.signupService = signupService;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel > T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel(signupService,mContext);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");    }


}
