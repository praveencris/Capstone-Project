package com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.ordersummary;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.User;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderSummaryViewModel extends ViewModel implements LifecycleObserver {
    // TODO: Implement the ViewModel
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceItems;
    private DatabaseReference mDatabaseReferenceUsers;
    private FirebaseAuth mFirebaseAuth;


    private List<OrderPerUser> mOrderPerUserList;

    private ValueEventListener mValueEventListenerItems;
    private ValueEventListener mValueEventListenerUsers;

    private HashMap<String, String> mItemIdPriceHashMap;

    private MutableLiveData<List<OrderPerUser>> mMutableLiveDataOrderPerUser;
    private MutableLiveData<HashMap<String, String>> mHashMapMutableLiveData;

    public LiveData<List<OrderPerUser>> getOrdersPerUser() {
        if (mMutableLiveDataOrderPerUser == null) {
            mMutableLiveDataOrderPerUser = new MutableLiveData<List<OrderPerUser>>();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
        return mMutableLiveDataOrderPerUser;
    }

    public LiveData<HashMap<String, String>> getItemIdPriceMap() {
        if (mHashMapMutableLiveData == null) {
            mHashMapMutableLiveData = new MutableLiveData<>();
        }
        return mHashMapMutableLiveData;
    }

    private void loadOrdersPerUser() {
        // Do an asynchronous operation to fetch users.
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void attachDatabaseReadListener() {

        DatabaseReference databaseReferenceUser = mFirebaseDatabase.getReference(mFirebaseAuth.getUid());
        mDatabaseReferenceItems = databaseReferenceUser.child("items");
        mDatabaseReferenceUsers = databaseReferenceUser.child("users");

        if (mValueEventListenerUsers == null) {
            mValueEventListenerUsers = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        mOrderPerUserList = new ArrayList<>();
                        for (DataSnapshot usersDataSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot userSnapshot = usersDataSnapshot.child("user");
                            DataSnapshot orderSnapshot = usersDataSnapshot.child("order");
                            User user = userSnapshot.getValue(User.class);
                            List<OrderDetail> orderDetailList = new ArrayList<>();
                            for (DataSnapshot order : orderSnapshot.getChildren()) {
                                OrderDetail orderDetail = order.getValue(OrderDetail.class);
                                orderDetailList.add(orderDetail);
                            }
                            OrderPerUser orderPerUser = new OrderPerUser(user.getUserID(), user.getUserName(), orderDetailList);
                            mOrderPerUserList.add(orderPerUser);
                        }
                        if (mMutableLiveDataOrderPerUser != null)
                            mMutableLiveDataOrderPerUser.setValue(mOrderPerUserList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mDatabaseReferenceUsers.addValueEventListener(mValueEventListenerUsers);
        }
        if (mValueEventListenerItems == null) {
            mValueEventListenerItems = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren()) {
                        for (Item itemInfo : Utils.getDummyList()) {
                            String id = mDatabaseReferenceItems.push().getKey();
                            Item item
                                    = new Item(id, itemInfo.getItemName(), String.valueOf(itemInfo.getItemPrice()), itemInfo.getMaxItemAllowed());
                            mDatabaseReferenceItems.child(id).setValue(item);
                        }
                    } else {
                        mItemIdPriceHashMap = new HashMap<>();
                        for (DataSnapshot dataSnapshotItem : dataSnapshot.getChildren()) {
                            Item item = dataSnapshotItem.getValue(Item.class);
                            mItemIdPriceHashMap.put(item.getItemId(), item.getItemPrice());
                        }
                        if (mHashMapMutableLiveData != null)
                            mHashMapMutableLiveData.setValue(mItemIdPriceHashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mDatabaseReferenceItems.addValueEventListener(mValueEventListenerItems);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void detachDatabaseReadListener() {
        if (mValueEventListenerUsers != null) {
            mDatabaseReferenceUsers.removeEventListener(mValueEventListenerUsers);
            mValueEventListenerUsers = null;
        }
        if (mValueEventListenerItems != null) {
            mDatabaseReferenceItems.removeEventListener(mValueEventListenerItems);
            mValueEventListenerItems = null;
        }
    }
}
