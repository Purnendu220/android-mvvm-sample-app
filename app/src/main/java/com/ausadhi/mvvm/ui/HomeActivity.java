package com.ausadhi.mvvm.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.databinding.ActivityHomeBinding;
import com.ausadhi.mvvm.ui.fragment.AddItemFragment;
import com.ausadhi.mvvm.ui.fragment.OrderListFragment;
import com.ausadhi.mvvm.ui.fragment.ProfileFragment;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.ReadExcel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityHomeBinding dataBinding;
    Context mContext;
    private Toolbar mToolbar;

    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(starter);
    }
    public static Intent getIntent(Context mContext) {
        Intent starter = new Intent(mContext, HomeActivity.class);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        mContext = this;
        setToolbar(dataBinding.appBarTool.toolbarContainer);

        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        openFragment(AddItemFragment.newInstance("", ""));
        dataBinding.bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    public void setToolbar(Toolbar toolbar){
        this.mToolbar =toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                openFragment(AddItemFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.create_order));

                return true;
            case R.id.navigation_sms:
                openFragment(OrderListFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.order_list));

                return true;
            case R.id.navigation_notifications:
                openFragment(ProfileFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.profile));

                return true;
        }
        return false;
    }
}
