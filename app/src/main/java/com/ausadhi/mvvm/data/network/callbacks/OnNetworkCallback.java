package com.ausadhi.mvvm.data.network.callbacks;

public interface OnNetworkCallback {
    void onApiSuccess(int type,Object response);
    void onApiFailure(int type,Object response);

}
