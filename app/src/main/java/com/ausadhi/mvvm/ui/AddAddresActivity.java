package com.ausadhi.mvvm.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.AddressService;
import com.ausadhi.mvvm.data.network.services.ProductListService;
import com.ausadhi.mvvm.databinding.ActivityAddAddresBinding;
import com.ausadhi.mvvm.eventbus.EventBroadcastHelper;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.TextUtils;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddAddresActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddAddresBinding dataBinding;
    Context mContext;
    Toolbar mToolbar;
    RefrenceWrapper mRefrenceWraper;
    private TextUtils mTextUtils;
    AddressService mAddressService;
    private CollectionReference mUserAddressCollection;
    private DatabaseReference mUserAddressDocumentRefrence;
    UserModel user;
    AddressModel addressModel;

    public static void open(Context mContext) {
        Intent starter = new Intent(mContext, AddAddresActivity.class);
        mContext.startActivity(starter);
    }
    public static void open(Context mContext,AddressModel model) {
        Intent starter = new Intent(mContext, AddAddresActivity.class);
        starter.putExtra(AppConstants.IntentKeys.ADDRESS_MODAL,model);
        mContext.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = ActivityAddAddresBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        mContext = this;
        if(getIntent()!=null){
            addressModel  = (AddressModel) getIntent().getSerializableExtra(AppConstants.IntentKeys.ADDRESS_MODAL);
        }

        mRefrenceWraper = RefrenceWrapper.getInstance(mContext);
        user = DataManager.getInstance().getPrefs().getUser();
        mTextUtils = mRefrenceWraper.getTextUtils();
        mAddressService = AddressService.getInstance();
        mUserAddressCollection = AddressService.getInstance().getmUserAddressCollection();
        mUserAddressDocumentRefrence = AddressService.getInstance().getmUserAddressDocumentRefrence();
        setToolbar(dataBinding.layoutToolbar.toolbar);
        setTitle(getString(R.string.add_address));
        dataBinding.layoutAddress.cirAddAddressButton.setOnClickListener(this);

        if(addressModel!=null){
            setTitle(getString(R.string.edit_address));
            dataBinding.layoutAddress.cirAddAddressButton.setText("Update Address");
            setAddress();

        }else{
            setTitle(getString(R.string.add_address));

        }

    }

    public void setToolbar(Toolbar toolbar){
        mToolbar =toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setBackNavigationFinish();
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);


    }
    public void setBackNavigationFinish()
    {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setTitle(String title){
        getSupportActionBar().setTitle(title);

    }
    public void hideHomeAsUpIndicatorIcon(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.cirAddAddressButton:
        if(addressModel!=null){
           updateAddress();
        }else{
            addAddress();

        }
        break;
}
    }

    private void setAddress(){
       dataBinding.layoutAddress.editTextName.setText(addressModel.getName()!=null?addressModel.getName():"");
    dataBinding.layoutAddress.editTextPhone.setText(addressModel.getPhone()!=null?addressModel.getPhone():"");
     dataBinding.layoutAddress.editTextPincode.setText(addressModel.getPincode()!=null?addressModel.getPincode():"");
     dataBinding.layoutAddress.editTextHouseNo.setText(addressModel.getHouseno()!=null?addressModel.getHouseno():"");
    dataBinding.layoutAddress.editTextRoadName.setText(addressModel.getRoadname()!=null?addressModel.getRoadname():"");
    dataBinding.layoutAddress.editTextCityName.setText(addressModel.getCity()!=null?addressModel.getCity():"");
   dataBinding.layoutAddress.editTextState.setText(addressModel.getState()!=null?addressModel.getState():"");
   dataBinding.layoutAddress.editTextLandmark.setText(addressModel.getLandmark()!=null?addressModel.getLandmark():"");
    }

    private void addAddress(){
        dataBinding.layoutAddress.cirAddAddressButton.setEnabled(false);
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        String name = dataBinding.layoutAddress.editTextName.getText().toString();
        String phone = dataBinding.layoutAddress.editTextPhone.getText().toString();
        String pincode = dataBinding.layoutAddress.editTextPincode.getText().toString();
        String houseno = dataBinding.layoutAddress.editTextHouseNo.getText().toString();
        String roadname = dataBinding.layoutAddress.editTextRoadName.getText().toString();
        String city = dataBinding.layoutAddress.editTextCityName.getText().toString();
        String state = dataBinding.layoutAddress.editTextState.getText().toString();
        String landmark = dataBinding.layoutAddress.editTextLandmark.getText().toString();
        if(!mTextUtils.isEmpty(name,dataBinding.layoutAddress.editTextName)&&
                !mTextUtils.isEmpty(phone,dataBinding.layoutAddress.editTextPhone)&&
                !mTextUtils.isEmpty(pincode,dataBinding.layoutAddress.editTextPincode)&&
                !mTextUtils.isEmpty(houseno,dataBinding.layoutAddress.editTextHouseNo)&&
                !mTextUtils.isEmpty(roadname,dataBinding.layoutAddress.editTextRoadName)&&
                !mTextUtils.isEmpty(city,dataBinding.layoutAddress.editTextCityName)&&
                !mTextUtils.isEmpty(state,dataBinding.layoutAddress.editTextState)){
            String id = mUserAddressCollection.document().getId();
            AddressModel address = new AddressModel(id,name,phone,pincode,houseno,roadname,city,state,landmark);
            mUserAddressCollection.document(id).set(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dataBinding.layoutAddress.cirAddAddressButton.setEnabled(true);
                    dataBinding.KLoadingSpin.stopAnimation();
                    if(task.isSuccessful()){
                        List<AddressModel> list = DataManager.getInstance().getPrefs().getAdressList();
                        if(list==null){
                            list = new ArrayList<>();
                        }
                        boolean found=false;
                        for (AddressModel model:list) {
                            if(model.getId().equalsIgnoreCase(address.getId())){
                               found=true;
                            }
                        }
                        if(!found){
                            list.add(address);
                            DataManager.getInstance().getPrefs().saveAddressList(list);
                        }
                        ToastUtils.showSuccessToast(mContext,"Address add successfully.");
                        EventBroadcastHelper.onNewAddressAdded();
                        finish();

                    }else{
                        String error = task.getException().getMessage();
                        ToastUtils.showErrorToast(mContext,error);


                    }
                }
            });



        }else{
            dataBinding.layoutAddress.cirAddAddressButton.setEnabled(true);
            dataBinding.KLoadingSpin.stopAnimation();


        }


    }

  private void updateAddress(){

      dataBinding.layoutAddress.cirAddAddressButton.setEnabled(false);
      dataBinding.KLoadingSpin.startAnimation();
      dataBinding.KLoadingSpin.setIsVisible(true);
      String name = dataBinding.layoutAddress.editTextName.getText().toString();
      String phone = dataBinding.layoutAddress.editTextPhone.getText().toString();
      String pincode = dataBinding.layoutAddress.editTextPincode.getText().toString();
      String houseno = dataBinding.layoutAddress.editTextHouseNo.getText().toString();
      String roadname = dataBinding.layoutAddress.editTextRoadName.getText().toString();
      String city = dataBinding.layoutAddress.editTextCityName.getText().toString();
      String state = dataBinding.layoutAddress.editTextState.getText().toString();
      String landmark = dataBinding.layoutAddress.editTextLandmark.getText().toString();
      if(!mTextUtils.isEmpty(name,dataBinding.layoutAddress.editTextName)&&
              !mTextUtils.isEmpty(phone,dataBinding.layoutAddress.editTextPhone)&&
              !mTextUtils.isEmpty(pincode,dataBinding.layoutAddress.editTextPincode)&&
              !mTextUtils.isEmpty(houseno,dataBinding.layoutAddress.editTextHouseNo)&&
              !mTextUtils.isEmpty(roadname,dataBinding.layoutAddress.editTextRoadName)&&
              !mTextUtils.isEmpty(city,dataBinding.layoutAddress.editTextCityName)&&
              !mTextUtils.isEmpty(state,dataBinding.layoutAddress.editTextState)){
          String id =addressModel.getId();
          AddressModel address = new AddressModel(id,name,phone,pincode,houseno,roadname,city,state,landmark);
          mUserAddressCollection.document(id).set(address).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  dataBinding.layoutAddress.cirAddAddressButton.setEnabled(true);
                  dataBinding.KLoadingSpin.stopAnimation();
                  if(task.isSuccessful()){
                      ToastUtils.showSuccessToast(mContext,"Address updated successfully.");
                      //EventBroadcastHelper.onNewAddressAdded();
                      finish();

                  }else{
                      String error = task.getException().getMessage();
                      ToastUtils.showErrorToast(mContext,error);


                  }
              }
          });



      }else{
          dataBinding.layoutAddress.cirAddAddressButton.setEnabled(true);
          dataBinding.KLoadingSpin.stopAnimation();


      }



  }
}
