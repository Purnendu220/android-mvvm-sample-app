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
import com.ausadhi.mvvm.databinding.DialogConfirmationBinding;
import com.ausadhi.mvvm.databinding.DialogOrderConfirmationBinding;



public class ConfirmationDialog extends Dialog implements View.OnClickListener {
    private final int type;
    private OnAlertButtonAction listener;
    private Context mContext;
    private String title;
    private String message;
    private DialogConfirmationBinding dataBinding;
    private Object data;

    public ConfirmationDialog(@NonNull Context context, OnAlertButtonAction listener,String title, String message,int type) {
        super(context);
        this.listener = listener;
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.type = type;
        init();
    }

    public ConfirmationDialog(@NonNull Context context, OnAlertButtonAction listener,String title, String message,int type,Object data) {
        super(context);
        this.listener = listener;
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.type = type;
        this.data = data;
        init();
    }

    private void init(){
        dataBinding = DialogConfirmationBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        dataBinding.textViewCancel.setOnClickListener(this);
        dataBinding.textViewOk.setOnClickListener(this);
        dataBinding.textViewMessage.setText(message);
        dataBinding.textViewTitle.setText(title);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOk:
                listener.onOkClick(type,data);
                dismiss();
                break;
            case R.id.textViewCancel:
                dismiss();
                break;
        }
    }
}
