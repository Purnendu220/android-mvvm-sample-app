package com.ausadhi.mvvm.eventbus;



public class EventBroadcastHelper {


    public static void onNewAddressAdded(){
        GlobalBus.getBus().post(new Events.OnNewAddressAdd());

    }





}
