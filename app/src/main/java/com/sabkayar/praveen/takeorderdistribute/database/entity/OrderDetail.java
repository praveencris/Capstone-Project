package com.sabkayar.praveen.takeorderdistribute.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class OrderDetail implements Parcelable {


    @PrimaryKey
    @NonNull
    private String userName;

    private String itemDesc;

    protected OrderDetail(Parcel in) {
        userName = in.readString();
        itemDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(itemDesc);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public OrderDetail(String userName, String itemDesc) {
        this.userName = userName;
        this.itemDesc = itemDesc;
    }
}
