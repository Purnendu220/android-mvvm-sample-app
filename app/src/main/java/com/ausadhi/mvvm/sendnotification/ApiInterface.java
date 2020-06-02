package com.ausadhi.mvvm.sendnotification;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key=AAAA5uZeuVM:APA91bHjvu4GMRyve1TllwO9dejUXgvh3aDV-Rmy53JLSNLRySPAJu7CFflatYUOhyXBzLgCDYaJEfsjPg9CS1hCbxerPv-pJocBCI6nqKUcAmW-_6loizJNQ_EUcwitMAWte5LHOTPy",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);
}