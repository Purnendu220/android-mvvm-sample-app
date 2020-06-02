package com.ausadhi.mvvm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtils {

    public static String toJson(Object object) {

        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> classType) {

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, classType);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, typeOfT);
    }

}
