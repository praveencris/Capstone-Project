package com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.model;

import java.util.List;

public class OrderSummaryItem {
    private String itemID;
    private String itemName;
    private int itemCount;
    private List<String> userCounts;

    public OrderSummaryItem(String itemID, String itemName, int itemCount, List<String> userCounts) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.userCounts = userCounts;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
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

    public List<String> getUserCounts() {
        return userCounts;
    }

    public void setUserCounts(List<String> userCounts) {
        this.userCounts = userCounts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderSummaryItem{");
        sb.append("itemID='").append(itemID).append('\'');
        sb.append(", itemName='").append(itemName).append('\'');
        sb.append(", itemCount=").append(itemCount);
        sb.append(", userCounts=").append(userCounts);
        sb.append('}');
        return sb.toString();
    }
}
