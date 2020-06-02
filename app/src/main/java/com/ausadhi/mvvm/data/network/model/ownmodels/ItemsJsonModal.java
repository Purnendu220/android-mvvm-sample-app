package com.ausadhi.mvvm.data.network.model.ownmodels;

public class ItemsJsonModal {
    String itemName;

    public ItemsJsonModal(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
