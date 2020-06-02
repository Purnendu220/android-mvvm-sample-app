package com.ausadhi.mvvm.utils;

import android.content.Context;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GetValuesFromAssets {

    public static String getValuesFromAssets(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("items.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }
}
