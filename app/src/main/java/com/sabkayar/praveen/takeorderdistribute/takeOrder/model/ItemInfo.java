package com.sabkayar.praveen.takeorderdistribute.takeOrder.model;

public class ItemInfo {
    private String itemId;
    private String itemName;
    private String itemPrice;
    private int maxItemAllowed;
    private boolean isChecked;
    private int itemsSelected;

    public ItemInfo(String itemId, String itemName, String itemPrice, int maxItemAllowed, boolean isChecked, int itemsSelected) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
        this.isChecked = isChecked;
        this.itemsSelected = itemsSelected;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getItemsSelected() {
        return itemsSelected;
    }

    public void setItemsSelected(int itemsSelected) {
        this.itemsSelected = itemsSelected;
    }
}
