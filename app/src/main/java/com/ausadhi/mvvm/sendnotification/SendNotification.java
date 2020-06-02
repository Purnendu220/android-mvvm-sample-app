package com.ausadhi.mvvm.sendnotification;

import android.util.Log;

import androidx.annotation.Nullable;

import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.SignupService;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.okhttp.ResponseBody;

import retrofit2.Callback;

public class SendNotification {


    public static void sendNotificationToPatner(String token,String title,String body) {

        SendNotificationModel sendNotificationModel = new SendNotificationModel(body, title);
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken(token);
        SendMessageModal message = new SendMessageModal();
        message.setSendNotificationModel(requestNotificaton);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("kkkk","done");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public static void getUserAndSendNotification(String path,String title,String message){
        String[] arr =path.split("/");
        if(arr!=null&&arr.length>1){
            String userId = arr[1];
            SignupService.getInstance().getDatabasepRefrence().document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    UserModel model= CommonUtils.getUserModel(documentSnapshot.getData());
                    if(model.getFirebaseToken()!=null&&!model.getFirebaseToken().isEmpty()){
                        sendNotificationToPatner(model.getFirebaseToken(),title,message);
                    }

                }
            });






        }

    }

}
