package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProductListService {
    private static ProductListService instance;
    private final CollectionReference mProductDatabaseCollection;
    private final DatabaseReference mProductDatabaseRefrence;

    public static ProductListService getInstance() {
        if (instance == null) {
            instance = new ProductListService();
        }
        return instance;
    }
    private ProductListService() {
        mProductDatabaseCollection =  FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.PRODUCT_REFRENCE);
        mProductDatabaseRefrence = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.PRODUCT_REFRENCE);
    }

    public CollectionReference getProductDataCollectionRefrence(){
        return mProductDatabaseCollection;
    }
    public DatabaseReference getProductDatabaseRefrence(){
        return mProductDatabaseRefrence;
    }


}
