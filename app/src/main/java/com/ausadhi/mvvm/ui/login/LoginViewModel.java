package com.ausadhi.mvvm.ui.login;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserData;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.ui.base.BaseViewModel;

import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.TextUtils;
import com.ausadhi.mvvm.utils.ToolsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class LoginViewModel extends BaseViewModel {
    private MutableLiveData<UserModel> userLiveData ;
    private MutableLiveData<Boolean> isLoading ;



    private LoginService loginService;
    private Context mContext;
    RefrenceWrapper mRefrenceWraper;
    TextUtils mTextUtils;


    LoginViewModel(LoginService loginService,Context mContext) {
        this.loginService = loginService;
        userLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();

        this.mContext = mContext;
        mRefrenceWraper = RefrenceWrapper.getInstance(mContext);
        mTextUtils = mRefrenceWraper.getTextUtils();
    }



    private void setIsLoading(boolean loading) {
        isLoading.postValue(loading);
    }

    private void setUser(UserModel user) {
        setIsLoading(false);
        this.userLiveData.postValue(user);
    }

    MutableLiveData<UserModel> getUserLoginStatus() {
        return userLiveData;
    }

    MutableLiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }

    public void login(com.ausadhi.mvvm.databinding.ActivityLoginBinding binding) {
        String email = binding.layoutLogin.editTextEmail.getText().toString();
        String password = binding.layoutLogin.editTextPassword.getText().toString();
        if(!mTextUtils.isEmpty(email,binding.layoutLogin.editTextEmail)&&
                !mTextUtils.isEmpty(password,binding.layoutLogin.editTextPassword)&&
                mTextUtils.isEmailValid(email,binding.layoutLogin.editTextEmail)){
            setIsLoading(true);

            loginService.getauthRefrence().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loginService.getDatabaseRefrence().child(loginService.getauthRefrence().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel user = dataSnapshot.getValue(UserModel.class);
                                DataManager.getInstance().getPrefs().setUser(user);
                                setUser(user);
                                setIsLoading(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext,databaseError.getMessage(),Toast.LENGTH_LONG);
                                setIsLoading(false);

                            }
                        });
                    }else{
                        String error = task.getException().getMessage();
                        setIsLoading(false);
                        Toast.makeText(mContext,error,Toast.LENGTH_LONG);

                    }
                }
            });

    }



}
}
