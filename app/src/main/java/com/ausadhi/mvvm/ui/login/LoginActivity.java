package com.ausadhi.mvvm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.databinding.ActivityLoginBinding;
import com.ausadhi.mvvm.ui.AdminActivity;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.ausadhi.mvvm.ui.signup.SignupActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.bumptech.glide.Glide;

public class LoginActivity extends BaseActivity<LoginViewModel> implements View.OnClickListener {


    private ActivityLoginBinding viewBinding;


    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(starter);
    }


    @NonNull
    @Override
    protected LoginViewModel createViewModel() {
        LoginViewModelFactory factory = new LoginViewModelFactory(DataManager.getInstance().getLoginService(),mContext);
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @NonNull
    @Override
    protected void setUpToolbar() {
        // setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(mContext.getResources().getString(R.string.login));
//     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//     getSupportActionBar().setDisplayShowHomeEnabled(true);
//     getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_google_logo);
        // toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.white));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewModel.getLoadingStatus().observe(this,new LoadingObserver());
        viewModel.getUserLoginStatus().observe(this,new LoginObserver());
        Glide.with(LoginActivity.this)
                .load(R.drawable.wave)
                .into(viewBinding.splashwave);
        viewBinding.layoutLogin.cirLoginButton.setOnClickListener(this);
        viewBinding.layoutLogin.txtSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.cirLoginButton:
        viewModel.login(viewBinding);

        break;
    case R.id.txtSignUp:
        SignupActivity.open(mContext);
        finish();
        break;
    case R.id.forgetPassword:
        break;

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


    private class LoginObserver implements Observer<Object> {

        @Override
        public void onChanged(@Nullable Object data) {
            if(data !=null&& data instanceof UserModel){
                UserModel model = (UserModel) data;
                if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                    HomeActivity.open(mContext);
                }else{
                    AdminActivity.open(mContext);
                }
                ToastUtils.showSuccessToast(mContext,"Login Success");

            }else{
                if(data !=null&& data instanceof String){
                    ToastUtils.showErrorToast(mContext,(String)data);

                }else{
                    ToastUtils.showErrorToast(mContext,"Login Failed.Please try again later.");

                }

            }
        }
    }


}
