package com.ausadhi.mvvm.data.network.model;

import java.io.Serializable;
import java.util.List;

public class OrderModalResponse implements Serializable {
    String id;
    String adminId;
    String userIdRef;
    String orderDate;
    List<OrderItemModel> orderItems;
    AddressModel orderAddress;
    int orderStatus;
    String orderAmount;
    String orderMessage;
    String orderCancelReason;

    public OrderModalResponse(String id, String adminId, String userIdRef, String orderDate, List<OrderItemModel> orderItems, AddressModel orderAddress, int orderStatus, String orderAmount, String orderMessage, String orderCancelReason) {
        this.id = id;
        this.adminId = adminId;
        this.userIdRef = userIdRef;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.orderMessage = orderMessage;
        this.orderCancelReason = orderCancelReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public AddressModel getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(AddressModel orderAddress) {
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

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }
}
