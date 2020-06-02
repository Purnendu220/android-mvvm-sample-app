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
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ausadhi.mvvm.BuildConfig;
import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.CartProductAdapter;
import com.ausadhi.mvvm.adapters.OrderListAdapter;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.data.network.model.OrderModel;
import com.ausadhi.mvvm.data.network.services.OrderServiceAdmin;
import com.ausadhi.mvvm.data.network.services.OrderServiceUser;
import com.ausadhi.mvvm.databinding.FragmentOrderListBinding;
import com.ausadhi.mvvm.dialog.ConfirmationDialog;
import com.ausadhi.mvvm.dialog.OnAlertButtonAction;
import com.ausadhi.mvvm.sendnotification.SendNotification;
import com.ausadhi.mvvm.ui.OrderDetailActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
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


public class OrderListFragment extends Fragment implements AdapterCallBack, OnAlertButtonAction {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<OrderModalResponse> list = new ArrayList<>();
    FragmentOrderListBinding dataBinding;
    Context mContext;
    OrderListAdapter adapter;
    public OrderListFragment() {
    }

    public static OrderListFragment newInstance(String param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
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
        dataBinding = FragmentOrderListBinding.inflate(inflater,container,false);
        View view = dataBinding.getRoot();
        mContext  =getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initOrderAdapter();
      if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
          setDatabaseRefrenceChangeListenerUser();

      }else{
          setDatabaseRefrenceChangeListenerAdmin();

      }

    }
    private void initOrderAdapter()
    {
        adapter = new OrderListAdapter(this,mContext);
        dataBinding.orderList.setLayoutManager(new LinearLayoutManager(mContext));
        dataBinding.orderList.setHasFixedSize(false);
        try {
            ((SimpleItemAnimator) dataBinding.orderList.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        dataBinding.orderList.setAdapter(adapter);
        adapter.addAllItem(list);
        adapter.notifyDataSetChanged();
        checkEmptyLayout();
    }
    private void setDatabaseRefrenceChangeListenerUser(){
        OrderServiceUser.getInstance().getmUserOrderCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                 list = new ArrayList<>();
                for (DocumentSnapshot  snapShot:queryDocumentSnapshots.getDocuments()) {
                    LogUtils.networkError(snapShot.getId());
                    OrderModalResponse model= CommonUtils.getOrderFromHaspMap(snapShot.getData());
                    list.add(model);
                }
                if(adapter!=null){
                    adapter.clear();
                    adapter.addAllItem(list);
                    checkEmptyLayout();
                }

            }
        });
    }

    private void setDatabaseRefrenceChangeListenerAdmin(){
        OrderServiceAdmin.getInstance().getmUserOrderCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                list = new ArrayList<>();
                for (DocumentSnapshot snapShot:queryDocumentSnapshots.getDocuments()) {
                    LogUtils.networkError(snapShot.getId());
                    OrderModalResponse model= CommonUtils.getOrderFromHaspMap(snapShot.getData());
                    list.add(model);
                }
                if(adapter!=null){
                    adapter.clear();
                    adapter.addAllItem(list);
                    checkEmptyLayout();
                }
            }
        });
    }
    private void checkEmptyLayout(){
        if(adapter!=null){
            if(adapter.getItemCount()>0){
                dataBinding.orderList.setVisibility(View.VISIBLE);
                dataBinding.emptyView.getRoot().setVisibility(View.GONE);
            }else{
                dataBinding.orderList.setVisibility(View.GONE);
                dataBinding.emptyView.getRoot().setVisibility(View.VISIBLE);
                dataBinding.emptyView.textViewEmptyLayoutText.setText(mContext.getResources().getString(R.string.you_havent_placed_any_order));

            }
        }else{
            dataBinding.orderList.setVisibility(View.GONE);
            dataBinding.emptyView.getRoot().setVisibility(View.VISIBLE);


        }
    }

    @Override
    public void onItemClick(Object model, View view, int position) {
        OrderModalResponse data;
        switch (view.getId()){
            case R.id.linearLayoutUserCancel:
                ConfirmationDialog dialog = new ConfirmationDialog(mContext,this,mContext.getResources().getString(R.string.are_you_sure),mContext.getResources().getString(R.string.realy_want_to_cancel),AppConstants.CONFIRM_ORDER_CANCEL,model);
                dialog.show();
                break;
            case R.id.mainContainerOrder:
                OrderDetailActivity.open(mContext, (OrderModalResponse) model);
                break;
            case R.id.linearLayoutUserUpdate:
                 data = (OrderModalResponse) model;
                updateOrder(data,data.getOrderStatus()+1);
                break;
            case R.id.imgContactCustomer:
                data = (OrderModalResponse) model;
                makeCall(data);

                break;
            case R.id.imgCancelOrder:
                cancelOrderAdmin((OrderModalResponse) model);
                break;
            case R.id.imgShareOrder:
                data = (OrderModalResponse) model;
                CommonUtils.shareViaWhatsApp(mContext,data.getOrderItems());
                break;

        }
    }

    @Override
    public void onOkClick(int requestCode, Object data) {
        switch (requestCode){
            case AppConstants.CONFIRM_ORDER_CANCEL:
                cancelOrder((OrderModalResponse) data);

                break;
        }

    }

    @Override
    public void onCancelClick(int requestCode, Object data) {

    }

    private void cancelOrder(OrderModalResponse model){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
       OrderModel orderModel = CommonUtils.getOrderModelFromOrderModalResp(model);
       orderModel.setOrderStatus(AppConstants.OrderStatus.ORDER_CANCELED);
       OrderServiceAdmin.getInstance().getmUserOrderCollection().document(model.getAdminId()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    OrderServiceUser.getInstance().getOrderDocumentRefrence(model.getUserIdRef()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dataBinding.KLoadingSpin.stopAnimation();
                            if(task.isSuccessful()){
                                ToastUtils.showErrorToast(mContext,"Order Canceled successfully");

                            }
                        }
                    });
                }else{
                    dataBinding.KLoadingSpin.stopAnimation();
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }
    private void cancelOrderAdmin(OrderModalResponse model){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        OrderModel orderModel = CommonUtils.getOrderModelFromOrderModalResp(model);
        orderModel.setOrderStatus(AppConstants.OrderStatus.ORDER_CANCELED_BY_ADMIN);
        OrderServiceAdmin.getInstance().getmUserOrderCollection().document(model.getAdminId()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    OrderServiceUser.getInstance().getOrderDocumentRefrence(model.getUserIdRef()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dataBinding.KLoadingSpin.stopAnimation();
                            if(task.isSuccessful()){
                                ToastUtils.showErrorToast(mContext,"Order Canceled successfully");
                                SendNotification.getUserAndSendNotification(model.getUserIdRef(),"Order Update","Your order canceled by admin.");
                            }
                        }
                    });
                }else{
                    dataBinding.KLoadingSpin.stopAnimation();
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }
    private void updateOrder(OrderModalResponse model,int status){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        OrderModel orderModel = CommonUtils.getOrderModelFromOrderModalResp(model);
        orderModel.setOrderStatus(status);
        OrderServiceAdmin.getInstance().getmUserOrderCollection().document(model.getAdminId()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    OrderServiceUser.getInstance().getOrderDocumentRefrence(model.getUserIdRef()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dataBinding.KLoadingSpin.stopAnimation();
                            if(task.isSuccessful()){
                                ToastUtils.showErrorToast(mContext,"Order Updated successfully");
                                SendNotification.getUserAndSendNotification(model.getUserIdRef(),"Order Update","Your order updated check your order status.");


                            }
                        }
                    });
                }else{
                    dataBinding.KLoadingSpin.stopAnimation();
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }

    private void makeCall(OrderModalResponse data){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+data.getOrderAddress().getPhone()));//change the number
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
