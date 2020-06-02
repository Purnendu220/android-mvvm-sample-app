package com.ausadhi.mvvm.data.network.model;

public class ProductModel {
    String id;
    String productName;

    public ProductModel(String id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public ProductModel() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
