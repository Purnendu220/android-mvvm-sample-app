package com.ausadhi.mvvm.ui.login;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.model.UserData;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.fcm.FCMHandler;
import com.ausadhi.mvvm.ui.base.BaseViewModel;

import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.TextUtils;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.ausadhi.mvvm.utils.ToolsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class LoginViewModel extends BaseViewModel {
    private MutableLiveData<Object> userLiveData ;
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

    private void setUser(Object data) {
        setIsLoading(false);
        this.userLiveData.postValue(data);
    }

    MutableLiveData<Object> getUserLoginStatus() {
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

                        loginService.getmUserDatabase().document(task.getResult().getUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                LogUtils.networkError(documentSnapshot.getId());
                                UserModel model= CommonUtils.getUserModel(documentSnapshot.getData());
                                DataManager.getInstance().getPrefs().setUser(model);
                                setUser(model);
                                setIsLoading(false);
                                loginService.getmUserDatabase().document(loginService.getauthRefrence().getCurrentUser().getUid()).set(model);
                                FCMHandler.getInstance().getFCMToken();
                                CommonUtils.getProducts();


                            }
                        });

                    }else{
                        String error = task.getException().getMessage();
                        setUser(error);

                    }
                }
            });

    }



}
}
