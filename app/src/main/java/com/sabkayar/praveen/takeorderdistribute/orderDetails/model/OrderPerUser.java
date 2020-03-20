package com.sabkayar.praveen.takeorderdistribute.orderDetails.model;

import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;

import java.util.List;

public class OrderPerUser {
    private String userId;
    private String userName;
    private List<OrderDetail> orderDetails;

    public OrderPerUser(String userId, String userName, List<OrderDetail> orderDetails) {
        this.userId = userId;
        this.userName = userName;
        this.orderDetails = orderDetails;
    }

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
}
