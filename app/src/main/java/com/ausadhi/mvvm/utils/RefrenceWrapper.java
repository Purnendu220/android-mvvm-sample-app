package com.ausadhi.mvvm.utils;

import android.content.Context;

import com.ausadhi.mvvm.data.DataManager;

public class RefrenceWrapper {
    private static RefrenceWrapper sInstance;
    public Context mContext;
    private RefrenceWrapper() {
        // This class is not publicly instantiable
    }
    private RefrenceWrapper(Context mContext) {
        this.mContext = mContext;
    }

    public static RefrenceWrapper getInstance(Context mContext) {
        if (sInstance == null) {
            sInstance = new RefrenceWrapper(mContext);
        }
        return sInstance;
    }
    private TextUtils mTextUtils;

    public TextUtils getTextUtils() {
        if(mTextUtils==null){
           mTextUtils = new TextUtils(mContext);
        }
        return mTextUtils;
    }


}
