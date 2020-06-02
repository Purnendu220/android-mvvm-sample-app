package com.ausadhi.mvvm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.AddressListAdapter;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.services.AddressService;
import com.ausadhi.mvvm.databinding.LayoutBottomsheetListAddressBinding;
import com.ausadhi.mvvm.eventbus.Events;
import com.ausadhi.mvvm.eventbus.GlobalBus;
import com.ausadhi.mvvm.ui.AddAddresActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.FirebaseRemoteConfigHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class AddressListBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private Context context;
    private AddressListAdapter showAddressList;
    private static AdapterCallBack adapterCallBack;
    private List<AddressModel> mList;
    private AddressService mAddressService;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewAddressAdded(Events.OnNewAddressAdd data) {
        setShowAddressView();
    }


    public static AddressListBottomSheet newInstance(Context context,AdapterCallBack adapterCallBack) {
        AddressListBottomSheet bottomSheetFragment = new AddressListBottomSheet();
        bottomSheetFragment.adapterCallBack=adapterCallBack;
        return bottomSheetFragment;
    }
    LayoutBottomsheetListAddressBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = LayoutBottomsheetListAddressBinding.inflate(inflater,container,false);
        context=getActivity();
        GlobalBus.getBus().register(this);
        mList=new ArrayList<>();
        dataBinding.progressBar.setVisibility(View.GONE);
        mAddressService = AddressService.getInstance();
        dataBinding.textViewNoAddress.setOnClickListener(this);
        setShowAddressView();


        return dataBinding.getRoot();

    }
    private void setShowAddressView() {
        dataBinding.recycleViewShowAddress.setLayoutManager(new LinearLayoutManager(context));
        dataBinding.recycleViewShowAddress.setHasFixedSize(true);
        showAddressList= new AddressListAdapter(adapterCallBack, context);
        dataBinding.recycleViewShowAddress.setAdapter(showAddressList);
        showAddressList.addAllItem(DataManager.getInstance().getPrefs().getAdressList());
        showAddressList.notifyDataSetChanged();
        if(DataManager.getInstance().getPrefs().getAdressList()==null||DataManager.getInstance().getPrefs().getAdressList().isEmpty()){
            dataBinding.textViewNoAddress.setVisibility(View.VISIBLE);
        }else{
            dataBinding.textViewNoAddress.setVisibility(View.GONE);

        }
        String message = FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(context).getRemoteConfigValue(AppConstants.RemoteConfig.SELECT_ADDRESS_TITLE);
         dataBinding.textViewSelectAddress.setText(message);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewNoAddress:
                AddAddresActivity.open(context);

                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);

    }
}
