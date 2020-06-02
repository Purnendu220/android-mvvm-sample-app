package com.ausadhi.mvvm.fcm;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class FCMHandler {

    private static FCMHandler sInstance;

    private FCMHandler() {
        // This class is not publicly instantiable
    }

    public static synchronized FCMHandler getInstance() {
        if (sInstance == null) {
            sInstance = new FCMHandler();
        }
        return sInstance;
    }

    public void getFCMToken(){
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... params) {
                String token = FirebaseInstanceId.getInstance().getToken();
                // Used to get firebase token until its null so it will save you from null pointer exeption
                while(token == null) {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
                    DataManager.getInstance().getPrefs().setToken(token);
                    if(DataManager.getInstance().getPrefs().getUser()!=null){
                        UserModel model = DataManager.getInstance().getPrefs().getUser();
                        model.setFirebaseToken(token);
                        LoginService.getInstance().getmUserDatabase().document(model.getUserId()).set(model);
                    }

                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
            }
        }.execute();    }

    public void disableFCM(){
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Call your Activity where you want to land after log out
            }
        }.execute();
    }

}