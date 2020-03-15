package com.sabkayar.praveen.takeorderdistribute.realtimedbmodels;

public class Item {
    private String itemId;
    private String itemName;
    private String itemPrice;
    private int maxItemAllowed;

    public Item() {
    }

    public Item(String itemId, String itemName, String itemPrice, int maxItemAllowed) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
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

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getMaxItemAllowed() {
        return maxItemAllowed;
    }

    public void setMaxItemAllowed(int maxItemAllowed) {
        this.maxItemAllowed = maxItemAllowed;
    }
}
