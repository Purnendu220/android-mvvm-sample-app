package com.ausadhi.mvvm.data.network.services;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddressService {
    private static AddressService instance;
    private CollectionReference mUserAddressCollection;
    private DatabaseReference mUserAddressDocumentRefrence;


    public static AddressService getInstance() {
        if (instance == null) {
            instance = new AddressService();
        }
        return instance;
    }

    private AddressService() {
       // this.user = user;


        mUserAddressCollection =  FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.USER_ADDRESS_REFRENCE);
        mUserAddressCollection = mUserAddressCollection.document(DataManager.getInstance().getPrefs().getUser().getUserId()).collection(ApiConstants.Refrences.USER_ADDRESS_REFRENCE);

        mUserAddressDocumentRefrence = FirebaseDatabase.getInstance().getReference(ApiConstants.Refrences.USER_ADDRESS_REFRENCE);

        mUserAddressDocumentRefrence.child(DataManager.getInstance().getPrefs().getUser().getUserId()).child(ApiConstants.Refrences.USER_ADDRESS_REFRENCE);



    }

    public CollectionReference getmUserAddressCollection() {
        return mUserAddressCollection;
    }

    public void setmUserAddressCollection(CollectionReference mUserAddressCollection) {
        this.mUserAddressCollection = mUserAddressCollection;
    }

    public DatabaseReference getmUserAddressDocumentRefrence() {
        return mUserAddressDocumentRefrence;
    }

    public void setmUserAddressDocumentRefrence(DatabaseReference mUserAddressDocumentRefrence) {
        this.mUserAddressDocumentRefrence = mUserAddressDocumentRefrence;
    }
}
