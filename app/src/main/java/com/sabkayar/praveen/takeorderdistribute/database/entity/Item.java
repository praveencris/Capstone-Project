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

    @ColumnInfo(defaultValue = "4")
    private int maxItemAllowed;

    @Ignore
    public Item(@NonNull String itemName, int itemPrice, int maxItemAllowed) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
    }

    public Item(int itemId, @NonNull String itemName, int itemPrice, int maxItemAllowed) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.maxItemAllowed = maxItemAllowed;
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
}
