package com.ausadhi.mvvm.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ausadhi.mvvm.BuildConfig;
import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.AddressListAdapter;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.AddressService;
import com.ausadhi.mvvm.databinding.FragmentProfileBinding;
import com.ausadhi.mvvm.fcm.FCMHandler;
import com.ausadhi.mvvm.ui.AddAddresActivity;
import com.ausadhi.mvvm.ui.login.LoginActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.FirebaseRemoteConfigHelper;
import com.ausadhi.mvvm.utils.LogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements AdapterCallBack {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Context mContext;
    private FragmentProfileBinding dataBinding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AddressListAdapter showAddressList;
    private List<AddressModel> mList;
    private AddressService mAddressService;
   private UserModel user;
    private CollectionReference mUserAddressCollection;
    private DatabaseReference mUserAddressDocumentRefrence;
    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = FragmentProfileBinding.inflate(inflater,container,false);
        View view = dataBinding.getRoot();
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        setUserData();

        mAddressService = AddressService.getInstance();
        mUserAddressCollection = AddressService.getInstance().getmUserAddressCollection();
        mUserAddressDocumentRefrence = AddressService.getInstance().getmUserAddressDocumentRefrence();
        dataBinding.linearLayoutUserBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddresActivity.open(mContext);
            }
        });
        dataBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginActivity.open(mContext);
                DataManager.getInstance().getPrefs().clearPrefrence();
                FCMHandler.getInstance().disableFCM();

                getActivity().finish();
            }
        });

        dataBinding.contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              makeCall();
            }
        });
        setShowAddressView();
        setDatabaseRefrenceChangeListener();

    }
    private void setUserData(){
         user = DataManager.getInstance().getPrefs().getUser();
        dataBinding.textViewName.setText(user.getName());
        dataBinding.txtPhone.setText(user.getUserMobile());
        dataBinding.textViewEmail.setText(user.getUserEmail());
        if(DataManager.getInstance().getPrefs().getAdressList().size()>0){
            dataBinding.textViewAddress.setText(String.format(getString(R.string.your_address),DataManager.getInstance().getPrefs().getAdressList().size()+""));
        } else{
            dataBinding.textViewAddress.setVisibility(View.GONE);
        }



    }

    private void setShowAddressView() {

        dataBinding.recycleViewShowAddress.setLayoutManager(new LinearLayoutManager(mContext));
        dataBinding.recycleViewShowAddress.setHasFixedSize(true);
        showAddressList= new AddressListAdapter(this, mContext,AppConstants.OpenFrom.PROFILE_FRAGMENT);
        dataBinding.recycleViewShowAddress.setAdapter(showAddressList);
    }

    private void setDatabaseRefrenceChangeListener(){
        mUserAddressCollection.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<AddressModel> list = new ArrayList<>();
                for (DocumentSnapshot  snapShot:queryDocumentSnapshots.getDocuments()) {
                    LogUtils.networkError(snapShot.getId());
                    AddressModel model= CommonUtils.getAddressFromHaspMap(snapShot.getData(),snapShot.getId());
                    list.add(model);
                }
                DataManager.getInstance().getPrefs().saveAddressList(list);
                showAddressList.clear();
                showAddressList.addAllItem(DataManager.getInstance().getPrefs().getAdressList());
                showAddressList.notifyDataSetChanged();
                setUserData();
            }
        });
    }

    @Override
    public void onItemClick(Object model, View view, int position) {
        AddressModel data ;

        switch (view.getId()){
            case R.id.imgDelete:
                dataBinding.KLoadingSpin.startAnimation();
                dataBinding.KLoadingSpin.setIsVisible(true);
                 data = (AddressModel) model;
                mUserAddressCollection.document(data.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dataBinding.KLoadingSpin.stopAnimation();
                        if(task.isSuccessful()){
                            setDatabaseRefrenceChangeListener();

                        }
                    }
                });



                break;
            case R.id.imgEdit:
                 data = (AddressModel) model;
                 AddAddresActivity.open(mContext,data);

                break;

        }

    }

    private void makeCall(){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).getRemoteConfigValue(AppConstants.RemoteConfig.CONTACT_US)));//change the number
                        startActivity(callIntent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
