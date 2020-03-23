package com.sabkayar.praveen.takeorderdistribute.realtimedbmodels;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDetail implements Parcelable {
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

    protected OrderDetail(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        itemCount = in.readInt();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(itemName);
        dest.writeInt(itemCount);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderDetail{");
        sb.append("itemId='").append(itemId).append('\'');
        sb.append(", itemName='").append(itemName).append('\'');
        sb.append(", itemCount=").append(itemCount);
        sb.append('}');
        return sb.toString();
    }
}
