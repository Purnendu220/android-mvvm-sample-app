package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderServiceUser {
    private static OrderServiceUser instance;
    private CollectionReference mUserOrderCollection;

    private DatabaseReference mUserOrderDocumentRefrence;


    public static OrderServiceUser getInstance() {
        if (instance == null) {
            instance = new OrderServiceUser();
        }
        return instance;
    }

    private OrderServiceUser() {
        // this.user = user;

        mUserOrderCollection =  FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.USER_ORDER_REFERENCE);
        mUserOrderCollection = mUserOrderCollection.document(DataManager.getInstance().getPrefs().getUser().getUserId()).collection(ApiConstants.Refrences.USER_ORDER_REFERENCE);

        mUserOrderDocumentRefrence = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.USER_ORDER_REFERENCE);

        mUserOrderDocumentRefrence.child(DataManager.getInstance().getPrefs().getUser().getUserId()).child(ApiConstants.Refrences.USER_ORDER_REFERENCE);



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

    public  DocumentReference getOrderDocumentRefrence(String path){
        return FirebaseFirestore.getInstance().document(path);
    }
}
