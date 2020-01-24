package com.sabkayar.praveen.takeorderdistribute.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Item.class,
        parentColumns = "itemId",
        childColumns = "itemId",
        onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.CASCADE))
public class OrderDetail {

    @PrimaryKey(autoGenerate = true)
    private int orderId;

    private int itemId;

    @NonNull
    private String itemName;

    private int userId;

    @NonNull
    private String userName;

    private int itemCount;

    private Date orderDate;

    public OrderDetail(int orderId,int itemId, @NonNull String itemName, int userId, @NonNull String userName, int itemCount,Date orderDate) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.userName = userName;
        this.itemCount = itemCount;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    public int getUserId() {
        return userId;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}
