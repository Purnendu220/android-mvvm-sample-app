package com.ausadhi.mvvm.ui.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.ui.AdminActivity;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.ausadhi.mvvm.ui.login.LoginActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.FirebaseRemoteConfigHelper;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.TyperTextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashViewModel> {

    @BindView(R.id.splashBottol)
    ImageView splashBottol;

    @BindView(R.id.textTyper)
    TextView textTyper;



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
                .load(R.drawable.splash)
                .into(splashBottol);
        String message = FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).getRemoteConfigValue(AppConstants.RemoteConfig.SPLASH_MESSAGE);


        textTyper.setText(message);
        LogUtils.networkError(DataManager.getInstance().getPrefs().getToken());
        viewModel.getIsLoading().observe(this,new LoadingObserver());
        viewModel.startTimer();

    }

    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("SplashActivity", "getInstanceId failed", task.getException());
                        return;
                    }

                    String token = task.getResult().getToken();
                    if(!DataManager.getInstance().getPrefs().getToken().equalsIgnoreCase(token)){
                        DataManager.getInstance().getPrefs().setToken(token);
                        if(DataManager.getInstance().getPrefs().getUser()!=null){
                            UserModel model = DataManager.getInstance().getPrefs().getUser();
                            model.setFirebaseToken(token);
                            LoginService.getInstance().getmUserDatabase().document(model.getUserId()).set(model);
                        }
                    }

                    // Log and toast
                    Log.d("SplashActivity", token);
                });

    }

    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading == null) return;
            if (!isLoading) {
                if(DataManager.getInstance().getPrefs().getUser()!=null){
                    getToken();
                    FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).fetchRemoteConfig();

                    if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                        HomeActivity.open(mContext);

                    }else{
                        AdminActivity.open(mContext);

                    }

                }
                else{
                    LoginActivity.open(mContext);

                }

                finish();
            }
        }
    }

}
