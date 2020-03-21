package com.sabkayar.praveen.takeorderdistribute.realtimedbmodels;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String itemId;
    private String itemName;
    private int itemCount;

    public OrderDetail() {
    }

    public OrderDetail(String itemId, String itemName, int itemCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCount = itemCount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
