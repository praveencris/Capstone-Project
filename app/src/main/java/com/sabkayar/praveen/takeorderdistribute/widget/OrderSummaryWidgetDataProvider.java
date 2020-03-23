package com.sabkayar.praveen.takeorderdistribute.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.model.OrderSummaryItem;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class OrderSummaryWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;

    private String[] mDummyData=new String[]{"one","two","three","two","three","two","three","two","three","two","three","two","three"};
    public OrderSummaryWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
     //initializeData();
    }

    @Override
    public void onDataSetChanged() {
       // initializeData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
      /*  if(mOrderSummaryItemsList!=null){
            return mOrderSummaryItemsList.size();
        }
        return 0;*/

      return mDummyData.length;

    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);

        remoteViews.setTextViewText(R.id.textViewTitle, mDummyData[position]);


      /*  remoteViews.setTextViewText(R.id.textViewTitle, mOrderSummaryItemsList.get(position).getItemName()+" - "+mOrderSummaryItemsList.get(position).getItemCount());
        String userAndCount="";
        for (String userCount:mOrderSummaryItemsList.get(position).getUserCounts()){
            userAndCount= String.format("%s%s\n", userAndCount, userCount);
        }
        remoteViews.setTextViewText(R.id.textViewSubTitle, userAndCount);*/
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private List<OrderPerUser> mOrderPerUserList;
    private HashMap<String, OrderSummaryItem> mOrderSummaryItemHashMap;
    private List<OrderSummaryItem>  mOrderSummaryItemsList = new ArrayList<>();;

    private void initializeData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid() != null) {
            return;
        }
        DatabaseReference databaseReferenceUser = firebaseDatabase.getReference(firebaseAuth.getUid());
        DatabaseReference databaseReferenceUsers = databaseReferenceUser.child("users");
        // Getting an iterator
        // Iterate through the hashmap
        // mOrderSummaryItemsList
        ValueEventListener valueEventListenerUsers = new ValueEventListener() {
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

                    mOrderSummaryItemHashMap = new HashMap<>();
                    for (OrderPerUser orderPerUser : mOrderPerUserList) {
                        for (OrderDetail orderDetail : orderPerUser.getOrderDetails()) {
                            if (mOrderSummaryItemHashMap.containsKey(orderDetail.getItemId())) {
                                OrderSummaryItem orderSummaryItem = mOrderSummaryItemHashMap.get(orderDetail.getItemId());
                                orderSummaryItem.setItemCount(orderSummaryItem.getItemCount() + orderDetail.getItemCount());
                                List<String> userNameCount;
                                userNameCount = orderSummaryItem.getUserCounts();
                                userNameCount.add(orderPerUser.getUserName() + "-" + orderDetail.getItemCount());
                                orderSummaryItem.setUserCounts(userNameCount);
                                mOrderSummaryItemHashMap.put(orderDetail.getItemId(), orderSummaryItem);
                            } else {
                                List<String> userNameCount = new ArrayList<>();
                                userNameCount.add(orderPerUser.getUserName() + "-" + orderDetail.getItemCount());
                                mOrderSummaryItemHashMap.put(orderDetail.getItemId(), new OrderSummaryItem(orderDetail.getItemId(), orderDetail.getItemName(), orderDetail.getItemCount(), userNameCount));
                            }
                        }
                    }
                    // Getting an iterator
                    Iterator hmIterator = mOrderSummaryItemHashMap.entrySet().iterator();
                    // Iterate through the hashmap
                    int totalAmount = 0;
                    while (hmIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) hmIterator.next();
                        OrderSummaryItem orderSummaryItem = (OrderSummaryItem) mapElement.getValue();
                        mOrderSummaryItemsList.add(orderSummaryItem);
                    }
                    // mOrderSummaryItemsList

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReferenceUsers.addValueEventListener(valueEventListenerUsers);
    }
}
