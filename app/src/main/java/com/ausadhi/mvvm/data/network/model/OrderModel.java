package com.ausadhi.mvvm.data.network.model;

public class OrderModel {
    String id;
    String adminId;
    String userIdRef;
    String orderDate;
    String orderItems;
    String orderAddress;
    int orderStatus;
    String orderAmount;
    String orderMessage;
    String orderCancelReason;

    public OrderModel(String id, String orderDate, String orderItems, String orderAddress, int orderStatus, String orderMessage) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderMessage = orderMessage;
    }

    public OrderModel(String id, String orderDate, String orderItems, String orderAddress, int orderStatus,String orderMessage, String adminId,String userIdRef,String orderAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.adminId = adminId;
        this.orderMessage = orderMessage;
        this.userIdRef = userIdRef;
        this.orderAmount = orderAmount;
    }

//    public OrderModel(String id, String orderDate, String orderItems, String orderAddress, int orderStatus, String orderAmount, String orderMessage, String orderCancelReason) {
//        this.id = id;
//        this.orderDate = orderDate;
//        this.orderItems = orderItems;
//        this.orderAddress = orderAddress;
//        this.orderStatus = orderStatus;
//        this.orderAmount = orderAmount;
//        this.orderMessage = orderMessage;
//        this.orderCancelReason = orderCancelReason;
//    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserIdRef() {
        return userIdRef;
    }

    public void setUserIdRef(String userIdRef) {
        this.userIdRef = userIdRef;
    }
}
