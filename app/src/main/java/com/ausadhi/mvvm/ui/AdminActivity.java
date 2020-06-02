package com.ausadhi.mvvm.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.model.ownmodels.ItemsJsonModal;
import com.ausadhi.mvvm.data.network.services.ProductListService;
import com.ausadhi.mvvm.databinding.ActivityAdminBinding;
import com.ausadhi.mvvm.fcm.FCMHandler;
import com.ausadhi.mvvm.sendnotification.SendNotification;
import com.ausadhi.mvvm.ui.fragment.AdminAddItemFragment;
import com.ausadhi.mvvm.ui.fragment.CreateAdminFragment;
import com.ausadhi.mvvm.ui.fragment.OrderListFragment;
import com.ausadhi.mvvm.ui.login.LoginActivity;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AdapterCallBack {
    ActivityAdminBinding dataBinding;
    Context mContext;
    private Toolbar mToolbar;

    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, AdminActivity.class);
        mContext.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        mContext = this;
        setToolbar(dataBinding.appBarTool.toolbarContainer);
        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        openFragment(AdminAddItemFragment.newInstance("", ""));
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
                openFragment(AdminAddItemFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.create_items));
                return true;
            case R.id.navigation_sms:
                openFragment(OrderListFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.order_list));

                return true;

            case R.id.navigation_create:
                openFragment(CreateAdminFragment.newInstance("", ""));
                getSupportActionBar().setTitle(mContext.getResources().getString(R.string.create));

                return true;

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                LoginActivity.open(mContext);
                DataManager.getInstance().getPrefs().clearPrefrence();
                FCMHandler.getInstance().disableFCM();
                finish();
                break;
        }



        return false;
    }
    @Override
    public void onItemClick(Object model, View view, int position) {

    }


}
