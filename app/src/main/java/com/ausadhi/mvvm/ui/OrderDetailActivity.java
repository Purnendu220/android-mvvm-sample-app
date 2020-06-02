package com.ausadhi.mvvm.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.CartProductAdapter;
import com.ausadhi.mvvm.adapters.MaterialSpinnerAdapter;
import com.ausadhi.mvvm.adapters.OrderItemList;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.data.network.model.OrderModel;
import com.ausadhi.mvvm.data.network.services.AddressService;
import com.ausadhi.mvvm.data.network.services.OrderServiceAdmin;
import com.ausadhi.mvvm.data.network.services.OrderServiceUser;
import com.ausadhi.mvvm.databinding.ActivityOrderDetailBinding;
import com.ausadhi.mvvm.sendnotification.SendNotification;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tiper.MaterialSpinner;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements AdapterCallBack, View.OnClickListener {
    ActivityOrderDetailBinding dataBinding;
    Context mContext;
    RefrenceWrapper mRefrenceWraper;
    Toolbar mToolbar;
    OrderItemList adapter;
    OrderModalResponse orderModel;
    List<OrderItemModel> mList;
    AddressModel addressModel;
    public static void open(Context mContext, OrderModalResponse model) {
        Intent starter = new Intent(mContext, OrderDetailActivity.class);
        starter.putExtra(AppConstants.IntentKeys.ORDER_MODAL,model);
        mContext.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        mContext = this;
        mRefrenceWraper = RefrenceWrapper.getInstance(mContext);
        if(getIntent()!=null){
            orderModel = (OrderModalResponse) getIntent().getSerializableExtra(AppConstants.IntentKeys.ORDER_MODAL);
        }
        mList = orderModel.getOrderItems();
        addressModel = orderModel.getOrderAddress();
        setToolbar(dataBinding.layoutToolbar.toolbar);
        setTitle("Order Detail");
        dataBinding.textOrderId.setText(orderModel.getAdminId());
        dataBinding.textOrderItemTitles.setText("Order Items("+orderModel.getOrderItems().size()+")");
        setOrderStatusAdapter();
        initCartProductAdapter();
        setAddress();
        dataBinding.btnUpdateOrder.setOnClickListener(this);

        if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.ADMIN)){
            dataBinding.btnUpdateOrder.setText("Update Order");
        }else{
            dataBinding.btnUpdateOrder.setText("Cancel Order");
            dataBinding.spinnerOrderStatus.setDrawable(null,false);
            if(orderModel.getOrderStatus()==AppConstants.OrderStatus.ORDER_CANCELED||orderModel.getOrderStatus()==AppConstants.OrderStatus.ORDER_CANCELED_BY_ADMIN){
                dataBinding.btnUpdateOrder.setVisibility(View.GONE);
            }
        }



    }
    private void setOrderStatusAdapter(){
        String statusArr[] = getResources().getStringArray(R.array.order_status);
        dataBinding.spinnerOrderStatus.setAdapter(new MaterialSpinnerAdapter(statusArr,mContext));
        dataBinding.spinnerOrderStatus.setSelection(orderModel.getOrderStatus());
        if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.ADMIN)){
            dataBinding.spinnerOrderStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                    if(i>0){
                        orderModel.setOrderStatus(i);
                    }else{
                        dataBinding.spinnerOrderStatus.setSelection(orderModel.getOrderStatus());
                        ToastUtils.showErrorToast(mContext,"Invalid status selected");
                    }

                }

                @Override
                public void onNothingSelected(MaterialSpinner materialSpinner) {

                }
            });

        }else{
            dataBinding.spinnerOrderStatus.setEnabled(false);
        }

        }


    private void setAddress(){
            dataBinding.textAddressName.setText(addressModel.getName());
            dataBinding.textAddressPhone.setText(addressModel.getPhone());
            dataBinding.textAddressHouseNo.setText(addressModel.getHouseno());
            dataBinding.textRoadName.setText(addressModel.getRoadname());

    }
    private void initCartProductAdapter()
    {
        adapter = new OrderItemList(this,mContext);
        dataBinding.listItems.setLayoutManager(new LinearLayoutManager(mContext));
        dataBinding.listItems.setHasFixedSize(false);
        try {
            ((SimpleItemAnimator) dataBinding.listItems.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        dataBinding.listItems.setAdapter(adapter);
        adapter.addAllItem(mList);
        adapter.notifyDataSetChanged();
        getOrderTotal();
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
    public void onItemClick(Object model, View view, int position) {
        switch (view.getId()){
            case R.id.editTextPrice:
                OrderItemModel data = (OrderItemModel) model;
                mList.get(position).setItemPrice(data.getItemPrice());
                getOrderTotal();
               // adapter.notifyDataSetChanged();


                break;
        }

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.btnUpdateOrder:
        if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
            cancelOrder(orderModel);
        }else{
            orderModel.setOrderItems(mList);
            double orderTotal = getOrderTotal();
            if(orderTotal>0){
                orderModel.setOrderAmount(orderTotal+"");
            }
            updateOrder(orderModel);
        }
       // SendNotification.sendNotificationToPatner()

        break;
    }
    }

    private double getOrderTotal(){
        double total = 0;
        for (OrderItemModel model:mList) {
            if(model.getItemPrice()!=null&&!model.getItemPrice().isEmpty()){
                total = total + Double.parseDouble(model.getItemPrice());
            }
        }
        if(total>0){
            dataBinding.cardOrderTotal.setVisibility(View.VISIBLE);
            dataBinding.textOrderAmount.setText(total+" RS");

        }else{
            dataBinding.cardOrderTotal.setVisibility(View.GONE);

        }
    return total;
    }

    private void updateOrder(OrderModalResponse model){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        OrderModel orderModel = CommonUtils.getOrderModelFromOrderModalResp(model);
        OrderServiceAdmin.getInstance().getmUserOrderCollection().document(model.getAdminId()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataBinding.KLoadingSpin.stopAnimation();

                if(task.isSuccessful()){
                    OrderServiceUser.getInstance().getOrderDocumentRefrence(model.getUserIdRef()).set(orderModel);
                    ToastUtils.showSuccessToast(mContext,"Order Updated successfully");
                    SendNotification.getUserAndSendNotification(model.getUserIdRef(),"Order Update","Your order updated check your order status.");

                }else{
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }
    private void cancelOrder(OrderModalResponse model){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        OrderModel orderModel = CommonUtils.getOrderModelFromOrderModalResp(model);
        orderModel.setOrderStatus(AppConstants.OrderStatus.ORDER_CANCELED);
        OrderServiceAdmin.getInstance().getmUserOrderCollection().document(model.getAdminId()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataBinding.KLoadingSpin.stopAnimation();
                if(task.isSuccessful()){
                    OrderServiceUser.getInstance().getOrderDocumentRefrence(model.getUserIdRef()).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dataBinding.btnUpdateOrder.setVisibility(View.GONE);
                                ToastUtils.showErrorToast(mContext,"Order Canceled successfully");

                            }
                        }
                    });
                }else{
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }
}
