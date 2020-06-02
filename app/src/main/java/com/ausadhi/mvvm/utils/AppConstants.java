package com.ausadhi.mvvm.utils;

/**
 * Created by Purnendu on 26/03/2020.
 */

public interface AppConstants {

     int CONFIRM_ORDER_FROM_DIALOG=1;
    int CONFIRM_ORDER_CANCEL=2;

    interface OpenFrom{
        int PROFILE_FRAGMENT = 1;



    }
     interface OrderStatus{
       int ORDER_PLACED = 1;
       int ORDER_ACCEPTED =2;
       int ORDER_PACKED = 3;
       int ORDER_DELIVERED =4;
       int ORDER_CANCELED  = 5;
       int ORDER_CANCELED_BY_ADMIN = 6;


     }
     interface IntentKeys{
         String ORDER_MODAL="order_modal";
         String ADDRESS_MODAL="address_modal";


     }
    interface UserType{
        String ADMIN = "ADMIN";
        String USER ="USER";

    }

    interface RemoteConfig{
        String CONTACT_US = "contact_us";
        String ORDER_CONFIRMATION = "order_confirmation";
        String SPLASH_MESSAGE = "splash_message";
        String CONTINUE_BUTTON = "continue_button";
        String SELECT_ADDRESS_TITLE = "select_address_title";


    }


}
