package com.ausadhi.mvvm.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.utils.RefrenceWrapper;

import butterknife.ButterKnife;

/**
 * Created by Purnendu on 07/01/2020.
 */
public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected VM viewModel;
    protected Activity activity;
    protected Context mContext;
    protected RefrenceWrapper mRefrenceWrapper;

    @NonNull
    protected abstract VM createViewModel();

    @NonNull
    protected abstract void setUpToolbar();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        mContext = this;
        mRefrenceWrapper = RefrenceWrapper.getInstance(mContext);

    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(activity);
        viewModel = createViewModel();
        setUpToolbar();

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(activity);
        viewModel = createViewModel();
        setUpToolbar();

    }

}
