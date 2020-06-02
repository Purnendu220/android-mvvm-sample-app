package com.ausadhi.mvvm.utils;

import android.content.Context;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

public class ToastUtils {

    public static void showSuccessToast(Context context,String message ){
        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
    }
    public static void showInfoToast(Context context,String message ){
        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
    }
    public static void showErrorToast(Context context,String message ){
        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
    }

    public static void showToast(Context context,String message ){
        Toast.makeText(context,message,FancyToast.LENGTH_LONG).show();
    }
    public static void showWarningToast(Context context,String message ){
        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
    }
    public static void showConfusionToast(Context context,String message ){
        FancyToast.makeText(context,message,FancyToast.LENGTH_LONG,FancyToast.CONFUSING,false).show();
    }
}
