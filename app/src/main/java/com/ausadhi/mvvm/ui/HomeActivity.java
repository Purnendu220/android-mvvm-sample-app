package com.ausadhi.mvvm.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ausadhi.mvvm.R;

public class HomeActivity extends AppCompatActivity {


    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
