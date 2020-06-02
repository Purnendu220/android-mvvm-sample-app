package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderServiceAdmin {
    private static OrderServiceAdmin instance;
    private CollectionReference mUserOrderCollection;
    private DatabaseReference mUserOrderDocumentRefrence;


    public static OrderServiceAdmin getInstance() {
        if (instance == null) {
            instance = new OrderServiceAdmin();
        }
        return instance;
    }

    private OrderServiceAdmin() {
        mUserOrderCollection =  FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.ADMIN_ORDER_REFERENCE);
        mUserOrderDocumentRefrence = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.ADMIN_ORDER_REFERENCE);




    }

    public CollectionReference getmUserOrderCollection() {
        return mUserOrderCollection;
    }

    public void setmUserOrderCollection(CollectionReference mUserOrderCollection) {
        this.mUserOrderCollection = mUserOrderCollection;
    }

    public DatabaseReference getmUserOrderDocumentRefrence() {
        return mUserOrderDocumentRefrence;
    }

    public void setmUserOrderDocumentRefrence(DatabaseReference mUserOrderDocumentRefrence) {
        this.mUserOrderDocumentRefrence = mUserOrderDocumentRefrence;
    }
}
