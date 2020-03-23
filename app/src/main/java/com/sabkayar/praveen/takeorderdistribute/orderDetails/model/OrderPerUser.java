package com.sabkayar.praveen.takeorderdistribute.orderDetails.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;

import java.util.List;

public class OrderPerUser implements Parcelable {
    private String userId;
    private String userName;
    private List<OrderDetail> orderDetails;

    public OrderPerUser(String userId, String userName, List<OrderDetail> orderDetails) {
        this.userId = userId;
        this.userName = userName;
        this.orderDetails = orderDetails;
    }

    protected OrderPerUser(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        orderDetails = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeTypedList(orderDetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderPerUser> CREATOR = new Creator<OrderPerUser>() {
        @Override
        public OrderPerUser createFromParcel(Parcel in) {
            return new OrderPerUser(in);
        }

        @Override
        public OrderPerUser[] newArray(int size) {
            return new OrderPerUser[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderPerUser{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", orderDetails=").append(orderDetails);
        sb.append('}');
        return sb.toString();
    }
}
