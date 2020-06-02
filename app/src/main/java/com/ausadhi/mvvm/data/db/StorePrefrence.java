package com.ausadhi.mvvm.data.db;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.constants.ApiConstants;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.JsonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorePrefrence {

    private static StorePrefrence sInstance;
    private final Preference prefrence;
    private Gson gson;


    private StorePrefrence() {
        gson=new Gson();
       prefrence = PowerPreference.getDefaultFile();
    }

    public static StorePrefrence getInstance() {
        if (sInstance == null) {
            sInstance = new StorePrefrence();
        }
        return sInstance;
    }

    public void setUser(UserModel user){
        prefrence.putObject(ApiConstants.Prefrences.USER_KEY,user);
    }
    public UserModel getUser(){
        return prefrence.getObject(ApiConstants.Prefrences.USER_KEY,UserModel.class,null);
    }

    public String getUserType(){
       UserModel model = prefrence.getObject(ApiConstants.Prefrences.USER_KEY,UserModel.class,null);
       if(model.getUserType()==null||model.getUserType().isEmpty()){
           return AppConstants.UserType.USER;
       }
       return model.getUserType();
    }
    public void saveProductList(List<ProductModel> mList){
        try {
            String productList = JsonUtils.toJson(mList);
            prefrence.putString(ApiConstants.Prefrences.PRODUCT_LIST_KEY,productList);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public List<ProductModel> getProductList(){
        try {
            String prodList = prefrence.getString(ApiConstants.Prefrences.PRODUCT_LIST_KEY);
            if (prodList != null) {
                List<ProductModel> productModels = gson.fromJson(prodList, new TypeToken<List<ProductModel>>() {
                }.getType());
                return productModels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void savecartList(List<OrderItemModel> items){
        try {
            String itemList = JsonUtils.toJson(items);
            prefrence.putString(ApiConstants.Prefrences.CART_LIST_KEY,itemList);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }
    public void clearCartList(){
        try {
            prefrence.remove(ApiConstants.Prefrences.CART_LIST_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }
    public List<OrderItemModel>  getCartList(){
        List<OrderItemModel> list =new ArrayList<>();
        try {
            String prodList = prefrence.getString(ApiConstants.Prefrences.CART_LIST_KEY);
            if (prodList != null&&!prodList.isEmpty()) {
                list = gson.fromJson(prodList, new TypeToken<List<OrderItemModel>>() {
                }.getType());
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
    public void saveAddressList(List<AddressModel> items){
        try {
            String addressList = JsonUtils.toJson(items);
            prefrence.putString(ApiConstants.Prefrences.ADDRESS_LIST_KEY,addressList);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public List<AddressModel> getAdressList(){
        List<AddressModel> productModels =new ArrayList<>();
        try {
            String prodList = prefrence.getString(ApiConstants.Prefrences.ADDRESS_LIST_KEY);
            if (prodList != null&&!prodList.isEmpty()) {
             productModels = gson.fromJson(prodList, new TypeToken<List<AddressModel>>() {
                }.getType());
                return productModels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModels;
    }

    public void clearPrefrence(){
        prefrence.clear();
    }







    public static <T> List<T> convertArrayToList(T array[])
    {
        if(array==null){
            return null; }
        List<T> list = Arrays.asList(array);
        return list;
    }


    public void setToken(String s) {
        prefrence.putString(ApiConstants.Prefrences.TOKEN,s);
    }
    public String getToken() {
       return prefrence.getString(ApiConstants.Prefrences.TOKEN,"");
    }
}
