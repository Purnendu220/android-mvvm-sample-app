package com.ausadhi.mvvm.dialog;

public interface OnAlertButtonAction {

        void onOkClick(int requestCode, Object data);

        void onCancelClick(int requestCode, Object data);
    }