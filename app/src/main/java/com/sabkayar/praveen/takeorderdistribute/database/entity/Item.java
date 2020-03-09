package com.sabkayar.praveen.takeorderdistribute.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int itemId;

    @NonNull
    private String itemName;

    @ColumnInfo(defaultValue = "0")
    private int itemPrice;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(@NonNull String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setMaxItemAllowed(int maxItemAllowed) {
        this.maxItemAllowed = maxItemAllowed;
    }

    @ColumnInfo(defaultValue = "4")
    private int maxItemAllowed;

    @ColumnInfo(defaultValue = "0")
    private int isChecked;


    @Ignore
    public Item(@NonNull String itemName, int itemPrice, int maxItemAllowed, int isChecked) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
        this.isChecked = isChecked;
    }

    public Item(int itemId, @NonNull String itemName, int itemPrice, int maxItemAllowed, int isChecked) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
        this.isChecked = isChecked;
    }

    public int getItemId() {
        return itemId;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getMaxItemAllowed() {
        return maxItemAllowed;
    }


    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }
}
