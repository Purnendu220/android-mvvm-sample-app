package com.ausadhi.mvvm.ui.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.ausadhi.mvvm.ui.login.LoginActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashViewModel> {

    @BindView(R.id.splashBottol)
    ImageView splashBottol;



    @NonNull
    @Override
    protected SplashViewModel createViewModel() {
        SplashViewModelFactory factory = new SplashViewModelFactory(mContext);
        return ViewModelProviders.of(this, factory).get(SplashViewModel.class);
    }

    @NonNull
    @Override
    protected void setUpToolbar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(activity);
        Glide.with(SplashActivity.this)
                .load(R.drawable.bottol)
                .into(splashBottol);



        viewModel.getIsLoading().observe(this,new LoadingObserver());
        viewModel.startTimer();
    }

    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading == null) return;
            if (!isLoading) {
                LoginActivity.open(mContext);
                finish();
            }
        }
    }

}
