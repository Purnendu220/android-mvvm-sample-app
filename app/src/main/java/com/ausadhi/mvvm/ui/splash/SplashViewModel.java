package com.ausadhi.mvvm.ui.splash;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.services.ProductListService;
import com.ausadhi.mvvm.ui.base.BaseViewModel;
import com.ausadhi.mvvm.utils.LogUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplashViewModel extends BaseViewModel {

    private Context mContext;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    SplashViewModel(Context mContext) {
        this.mContext = mContext;
    }

    void startTimer(){
        getProducts();
        isLoading.setValue(true);
        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {
                isLoading.setValue(false);
            }

        }, 5*1000);
    }

    void getProducts(){
        if(DataManager.getInstance().getPrefs().getUser()!=null){
            List<ProductModel> productModelList =new ArrayList<>();
            CollectionReference   mProductDatabaseCollection = ProductListService.getInstance().getProductDataCollectionRefrence();
            mProductDatabaseCollection.addSnapshotListener( new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot productSnapShot:queryDocumentSnapshots.getDocuments()) {
                        productModelList.add(new ProductModel(productSnapShot.getId(),(String) productSnapShot.getData().get("productName")));
                        LogUtils.debug((String) productSnapShot.getData().get("productName"));
                    }
                    DataManager.getInstance().getPrefs().saveProductList(productModelList);

                }
            });
        }

    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
