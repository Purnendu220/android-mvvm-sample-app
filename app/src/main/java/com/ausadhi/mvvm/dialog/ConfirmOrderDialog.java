package com.ausadhi.mvvm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.databinding.DialogOrderConfirmationBinding;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.FirebaseRemoteConfigHelper;

import java.util.List;

public class ConfirmOrderDialog extends Dialog implements View.OnClickListener {
    private OnAlertButtonAction listener;
    private Context mContext;
    private List<OrderItemModel> mList;
    private AddressModel addressModel;
    private DialogOrderConfirmationBinding dataBinding;

    public ConfirmOrderDialog(@NonNull Context context, OnAlertButtonAction listener,List<OrderItemModel> mList,AddressModel addressModel) {
        super(context);
        this.listener = listener;
        this.mContext = context;
        this.mList = mList;
        this.addressModel = addressModel;
        init();
    }

    private void init(){
        dataBinding = DialogOrderConfirmationBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        dataBinding.textViewCancel.setOnClickListener(this);
        dataBinding.textViewOk.setOnClickListener(this);
        ;
       String message = FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).getRemoteConfigValue(AppConstants.RemoteConfig.ORDER_CONFIRMATION);
        dataBinding.textViewMessage.setText(String.format(message,mList.size()+""));
          setAddressData();
    }

    private void setAddressData() {
        dataBinding.textAddressName.setText(addressModel.getName());
        dataBinding.textAddressPhone.setText(addressModel.getPhone());
        dataBinding.textAddressHouseNo.setText(addressModel.getHouseno());
        dataBinding.textRoadName.setText(addressModel.getRoadname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOk:
                listener.onOkClick(AppConstants.CONFIRM_ORDER_FROM_DIALOG,addressModel);
                dismiss();
                break;
            case R.id.textViewCancel:
                dismiss();
                break;
        }
    }
}
