package com.ausadhi.mvvm.ui.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.ui.AdminActivity;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.ausadhi.mvvm.ui.login.LoginActivity;
import com.ausadhi.mvvm.databinding.ActivitySignupBinding;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.ToastUtils;


public class SignupActivity  extends BaseActivity<SignupViewModel> implements View.OnClickListener {

    private ActivitySignupBinding viewBinding;

    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, SignupActivity.class);
        mContext.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.layoutRegister.btnRegister.setOnClickListener(this);
        viewBinding.layoutRegister.txtLogin.setOnClickListener(this);
        viewModel.getUserSignupStatus().observe(this,new UserObserver());
        viewModel.getLoadingStatus().observe(this,new LoadingObserver());

    }

    @NonNull
    @Override
    protected SignupViewModel createViewModel() {
        SignupViewModelFactory factory = new SignupViewModelFactory(DataManager.getInstance().getSignupService(),mContext);
        return ViewModelProviders.of(this, factory).get(SignupViewModel.class);
    }

    @NonNull
    @Override
    protected void setUpToolbar() {

    }


    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.btnRegister:
              viewModel.registerUser(viewBinding);
             // viewBinding.prosessLoader.startAnimation();
              //viewBinding.prosessLoader.setIsVisible(true);
              break;
          case R.id.txtLogin:
              LoginActivity.open(mContext);
              finish();
              break;
      }
    }

    private class UserObserver implements Observer<UserModel> {

        @Override
        public void onChanged(@Nullable UserModel users) {
            ToastUtils.showSuccessToast(mContext,"Signup Successful");
            if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                HomeActivity.open(mContext);
            }else{
                AdminActivity.open(mContext);
            }
            finish();

        }
    }
    //Observer
    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading == null) return;

            if (isLoading) {
                viewBinding.progressBar.setVisibility(View.VISIBLE);
            } else {
                viewBinding.progressBar.setVisibility(View.GONE);
            }
        }
    }
}
