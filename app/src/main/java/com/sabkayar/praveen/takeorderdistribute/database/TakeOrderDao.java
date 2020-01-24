package com.sabkayar.praveen.takeorderdistribute.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.database.entity.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.database.entity.UserName;

import java.util.List;

/*Let's write a DAO that provides queries for:

        1. Getting all/single Item ordered alphabetically by Item Name
        2. Inserting a Order,Item,User
        3. Deleting all Items,Orders,UserNames*/
@Dao
public interface TakeOrderDao {

    @Insert
    void insert(Item item);

    @Insert
    void insert(OrderDetail orderDetail);

    @Insert
    void insert(UserName userName);

    @Query("DELETE from OrderDetail")
    void deleteAllOrderDetails();

    @Query("DELETE from UserName")
    void deleteAllUserNames();

    @Query("DELETE from Item")
    void deleteAllItems();

    @Delete
    void deleteItem(Item item);

    @Delete
    void deleteOrderDetail(OrderDetail orderDetail);

    @Delete
    void deleteUserName(UserName userName);


    @Query("SELECT * FROM UserName ORDER BY userName")
    LiveData<List<UserName>> getUserNames();

    @Query("SELECT * FROM OrderDetail ORDER BY userName")
    LiveData<List<OrderDetail>> getOrderDetails();

    @Query("SELECT * FROM Item ORDER BY itemName")
    LiveData<List<Item>> getItems();


    @Query("SELECT * FROM Item where itemId=:itemId")
    Item getItem(int itemId);

    @Query("SELECT * FROM OrderDetail where orderId=:orderId")
    OrderDetail getOrderDetail(int orderId);

}
