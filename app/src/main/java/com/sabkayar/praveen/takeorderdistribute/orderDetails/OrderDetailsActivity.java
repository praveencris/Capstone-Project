package com.sabkayar.praveen.takeorderdistribute.orderDetails;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityOrderDetailsBinding;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.adapter.OrderDetailsAdapter;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.User;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.TakeOrderActivity;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.Utils;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDetailsAdapter.OnListItemListener {
    private static final int RC_TAKE_ORDER = 1;
    ActivityOrderDetailsBinding mBinding;
    private List<String> mNamesList;
    private List<User> mUserList;
    private OrderDetailsAdapter mOrderDetailsAdapter;
    private DatabaseReference mDatabaseReferenceItems;
    private DatabaseReference mDatabaseReferenceUsers;
    private DatabaseReference mDatabaseReferenceNames;

    private List<OrderPerUser> mOrderPerUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mBinding.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.tvLabel.setText(getString(R.string.number_of_orders_, 0));

        mDatabaseReferenceItems = FirebaseDatabase.getInstance().getReference("items");
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseReferenceNames = FirebaseDatabase.getInstance().getReference("names");
        mNamesList = new ArrayList<>();
        mUserList = new ArrayList<>();

        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mBinding.autoCompleteTvUserName.getText().toString().trim();
                if (!TextUtils.isEmpty(userName)) {
                    if (!ifNamesAvailableInUsersListGetID(userName).first) {
                        startActivityForResult(TakeOrderActivity.newIntent(OrderDetailsActivity.this, mBinding.autoCompleteTvUserName.getText().toString()), RC_TAKE_ORDER);
                    } else {
                        Toast.makeText(OrderDetailsActivity.this, "Order with same name already available,Please edit.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Enter user name to proceed further", Toast.LENGTH_SHORT).show();
                }
            }
        });

/*
        AppDatabase.getInstance(this).takeOrderDao().getItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if(items.size()==0){
                    Toast.makeText(OrderDetailsActivity.this,"Empty list",Toast.LENGTH_SHORT).show();
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            for(Item itemInfo: Utils.getDummyList()) {
                                String id = mDatabaseReferenceItems.push().getKey();
                                com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item item
                                        = new com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item(id, itemInfo.getItemName(), String.valueOf(itemInfo.getItemPrice()), itemInfo.getMaxItemAllowed());
                                mDatabaseReferenceItems.child(id).setValue(item);
                                Item itemLocal = new Item(itemInfo.getItemName(), itemInfo.getItemPrice(), itemInfo.getMaxItemAllowed(), itemInfo.getIsChecked());
                                AppDatabase.getInstance(OrderDetailsActivity.this).takeOrderDao().insert(itemLocal);
                            }
                        }
                    });
                }else {
                    Toast.makeText(OrderDetailsActivity.this,"Sit silently",Toast.LENGTH_SHORT).show();
                }
            }
        });
*/


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mOrderDetailsAdapter = new OrderDetailsAdapter(this);
        mBinding.recyclerView.setAdapter(mOrderDetailsAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mUserList.clear();
                    mOrderPerUserList = new ArrayList<>();
                    for (DataSnapshot usersDataSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot userSnapshot = usersDataSnapshot.child("user");
                        DataSnapshot orderSnapshot = usersDataSnapshot.child("order");
                        User user = userSnapshot.getValue(User.class);
                        mUserList.add(user);
                        List<OrderDetail> orderDetailList = new ArrayList<>();
                        for (DataSnapshot order : orderSnapshot.getChildren()) {
                            OrderDetail orderDetail = order.getValue(OrderDetail.class);
                            orderDetailList.add(orderDetail);
                        }
                        OrderPerUser orderPerUser = new OrderPerUser(user.getUserID(), user.getUserName(), orderDetailList);
                        mOrderPerUserList.add(orderPerUser);
                    }
                    mOrderDetailsAdapter.setItemInfoArrayList(mOrderPerUserList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabaseReferenceItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    for (Item itemInfo : Utils.getDummyList()) {
                        String id = mDatabaseReferenceItems.push().getKey();
                        com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item item
                                = new com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item(id, itemInfo.getItemName(), String.valueOf(itemInfo.getItemPrice()), itemInfo.getMaxItemAllowed());
                        mDatabaseReferenceItems.child(id).setValue(item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabaseReferenceNames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mNamesList.clear();
                    for (DataSnapshot usersDataSnapshot : dataSnapshot.getChildren()) {
                        mNamesList.add(usersDataSnapshot.getValue(String.class));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderDetailsActivity.this,
                            android.R.layout.simple_dropdown_item_1line, mNamesList);
                    mBinding.autoCompleteTvUserName.setAdapter(adapter);
                    mBinding.autoCompleteTvUserName.setThreshold(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private Pair<Boolean, String> ifNamesAvailableInUsersListGetID(String name) {
        for (User user : mUserList) {
            if (user.getUserName().equalsIgnoreCase(name)) {
                return new Pair<>(true, user.getUserID());
            }
        }
        return new Pair<>(false, null);
    }

    @Override
    public void onItemClick(OrderPerUser orderPerUser) {

    }
}
