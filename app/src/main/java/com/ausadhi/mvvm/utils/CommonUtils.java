package com.ausadhi.mvvm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.data.network.model.OrderModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.model.ownmodels.ItemsJsonModal;
import com.ausadhi.mvvm.data.network.services.ProductListService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommonUtils {
    public static AddressModel getAddressFromHaspMap(Map<String, Object> map, String dataId){
        String id=dataId;
        String name =map.get("name")!=null? map.get("name").toString():"" ;
        String phone= map.get("name")!=null?map.get("phone").toString():"" ;
        String pincode= map.get("name")!=null?map.get("pincode").toString():"" ;
        String houseno= map.get("name")!=null?map.get("houseno").toString():"" ;
        String roadname= map.get("name")!=null?map.get("roadname").toString():"";
        String city =map.get("name")!=null? map.get("city").toString():"";
        String state=map.get("name")!=null? map.get("state").toString():"";
        String landmark=map.get("name")!=null? map.get("landmark").toString():"" ;
        return new AddressModel(id,name,phone,pincode,houseno,roadname,city,state,landmark);
    }


    public static OrderModalResponse getOrderFromHaspMap(Map<String, Object> map){
        String id=map.get("id")!=null? map.get("id").toString():""  ;
        String adminId=map.get("adminId")!=null? map.get("adminId").toString():""  ;
        String userIdRef=map.get("userIdRef")!=null? map.get("userIdRef").toString():"" ;
        String orderDate=map.get("orderDate")!=null? map.get("orderDate").toString():"" ;
        List<OrderItemModel> orderItems = map.get("orderItems")!=null? JsonUtils.fromJson(map.get("orderItems").toString(),new TypeToken<List<OrderItemModel>>() {}.getType()):new ArrayList<>();
        AddressModel orderAddress = map.get("orderAddress")!=null?JsonUtils.fromJson(map.get("orderAddress").toString(),AddressModel.class):null ;
        int orderStatus =map.get("orderStatus")!=null? Integer.parseInt(map.get("orderStatus").toString()):0 ;
        String orderAmount =map.get("orderAmount")!=null? map.get("orderAmount").toString():"" ;
        String orderMessage=map.get("orderMessage")!=null? map.get("orderMessage").toString():"" ;
        String orderCancelReason=map.get("orderCancelReason")!=null? map.get("orderCancelReason").toString():"" ;

        return new OrderModalResponse(id,adminId,userIdRef,orderDate,orderItems,orderAddress,orderStatus,orderAmount,orderMessage,orderCancelReason);
    }

    public static UserModel getUserModel(Map<String, Object> map) {

        String userId =map.get("userId")!=null? map.get("userId").toString():"" ;
        String name= map.get("name")!=null?map.get("name").toString():"" ;
        String userMobile= map.get("userMobile")!=null?map.get("userMobile").toString():"" ;
        String userEmail= map.get("userEmail")!=null?map.get("userEmail").toString():"" ;
        String userPassword= map.get("userPassword")!=null?map.get("userPassword").toString():"";
        String userType =map.get("userType")!=null? map.get("userType").toString():"";
        String token = DataManager.getInstance().getPrefs().getToken();

        return new UserModel(userId,name,userMobile,userEmail,userPassword,userType,token);

    }

    public static UserModel getUserModelOthers(Map<String, Object> map) {

        String userId =map.get("userId")!=null? map.get("userId").toString():"" ;
        String name= map.get("name")!=null?map.get("name").toString():"" ;
        String userMobile= map.get("userMobile")!=null?map.get("userMobile").toString():"" ;
        String userEmail= map.get("userEmail")!=null?map.get("userEmail").toString():"" ;
        String userPassword= map.get("userPassword")!=null?map.get("userPassword").toString():"";
        String userType =map.get("userType")!=null? map.get("userType").toString():"";
        String token = map.get("firebaseToken")!=null? map.get("firebaseToken").toString():"";

        return new UserModel(userId,name,userMobile,userEmail,userPassword,userType,token);

    }

    public static OrderModel getOrderModelFromOrderModalResp(OrderModalResponse model) {
        OrderModel orderModel = new OrderModel(model.getId(),model.getOrderDate(), JsonUtils.toJson(model.getOrderItems()),JsonUtils.toJson(model.getOrderAddress()),model.getOrderStatus(),"",model.getAdminId(),model.getUserIdRef(),model.getOrderAmount());
        return orderModel;
    }
    public static void shareViaWhatsApp(Context mContext,List<OrderItemModel> list) {
        StringBuilder shareMsg=new StringBuilder();
        for (OrderItemModel item:list) {
            String message = "*Item Name:* "+item.getItemName()+" "+" *Quantity:* "+item.getItemQuantity()+"\n";
            shareMsg.append(message);

        }
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareMsg.toString());
        try {
            Objects.requireNonNull(mContext).startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
        }
    }

    public static String getItemsAsset(Context context) {
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

    public static void createItems(Context mContext){
        List<ItemsJsonModal> itemList = new ArrayList<>();
        try {
            itemList = new Gson().fromJson(CommonUtils.getItemsAsset(mContext), new TypeToken<List<ItemsJsonModal>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ItemsJsonModal model: itemList) {
            String id = ProductListService.getInstance().getProductDataCollectionRefrence().document().getId();
            ProductListService.getInstance().getProductDataCollectionRefrence().document(id).set(new ProductModel(id,model.getItemName())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });;
        }


    }

    public static void getProducts(){
        if(DataManager.getInstance().getPrefs().getUser()!=null){
            List<ProductModel> productModelList =new ArrayList<>();
            CollectionReference mProductDatabaseCollection = ProductListService.getInstance().getProductDataCollectionRefrence();
            mProductDatabaseCollection.addSnapshotListener( new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot productSnapShot:queryDocumentSnapshots.getDocuments()) {
                        productModelList.add(new ProductModel(productSnapShot.getId(),(String) productSnapShot.getData().get("productName")));
                        LogUtils.debug((String) productSnapShot.getData().get("productName"));
                    }
                    DataManager.getInstance().getPrefs().saveProductList(productModelList);

                }
            });
        }
    }



}
