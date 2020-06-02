package com.ausadhi.mvvm.sendnotification;

import com.google.gson.annotations.SerializedName;

public class SendMessageModal {


    @SerializedName("message")
    private RequestNotificaton sendMessageModel;

    public RequestNotificaton getSendNotificationModel() {
        return sendMessageModel;
    }

    public void setSendNotificationModel(RequestNotificaton sendMessageModel) {
        this.sendMessageModel = sendMessageModel;
    }



}
