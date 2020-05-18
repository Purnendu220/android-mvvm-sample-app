package com.ausadhi.mvvm.ui.signup;

import android.content.Context;
import android.provider.DocumentsProvider;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserData;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.data.network.services.SignupService;
import com.ausadhi.mvvm.databinding.ActivitySignupBinding;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.ui.base.BaseViewModel;
import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class SignupViewModel extends BaseViewModel {
    SignupService mSignupService;
    Context mContext;
    RefrenceWrapper mRefrenceWraper;
    TextUtils mTextUtils;
    private MutableLiveData<UserModel> userLiveData;
    private MutableLiveData<String> userRegistrationError;
    private MutableLiveData<Boolean> isLoading;


    SignupViewModel(SignupService signupService, Context mContext) {
        this.mSignupService = signupService;
        mRefrenceWraper = RefrenceWrapper.getInstance(mContext);
        mTextUtils = mRefrenceWraper.getTextUtils();
        userLiveData = new MutableLiveData<>();
        userRegistrationError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        this.mContext = mContext;
    }

    void registerUser(ActivitySignupBinding binding){
        String name = binding.layoutRegister.edtName.getText().toString();
        String mobile = binding.layoutRegister.edtMobile.getText().toString();
        String email = binding.layoutRegister.edtEmail.getText().toString();
        String password = binding.layoutRegister.edtPassword.getText().toString();

        if(!mTextUtils.isEmpty(name,binding.layoutRegister.edtName)&&
                !mTextUtils.isEmpty(mobile,binding.layoutRegister.edtMobile)&&
                !mTextUtils.isEmpty(email,binding.layoutRegister.edtEmail)&&
                !mTextUtils.isEmpty(password,binding.layoutRegister.edtPassword)&&
                 mTextUtils.isEmailValid(email,binding.layoutRegister.edtEmail)){
            setIsLoading(true);
              mSignupService.getauthRefrence().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          String id = mSignupService.getauthRefrence().getCurrentUser().getUid();
                          UserModel data = new UserModel(id,name,mobile,email,password);
                             mSignupService.getDatabasepRefrence().add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                 @Override
                                 public void onComplete(@NonNull Task<DocumentReference> task) {
                                     if(task.isSuccessful()){
                                         userLiveData.postValue(data);
                                         setIsLoading(false);
                                         DataManager.getInstance().getPrefs().setUser(data);

                                     }else{
                                         String error = task.getException().getMessage();
                                         userRegistrationError.postValue(error);
                                         setIsLoading(false);
                                         Toast.makeText(mContext,error,Toast.LENGTH_LONG);


                                     }
                                 }
                             });


                      }else {
                          String error = task.getException().getMessage();
                          userRegistrationError.postValue(error);
                          setIsLoading(false);
                          Toast.makeText(mContext,error,Toast.LENGTH_LONG);


                      }
                  }
              });



        }
    }
    private void setIsLoading(boolean loading) {
        isLoading.postValue(loading);
    }
    MutableLiveData<UserModel> getUserSignupStatus() {
        return userLiveData;
    }

    MutableLiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }
}
