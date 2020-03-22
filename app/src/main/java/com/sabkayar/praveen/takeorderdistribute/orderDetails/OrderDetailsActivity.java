package com.sabkayar.praveen.takeorderdistribute.orderDetails;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Arrays;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDetailsAdapter.OnListItemListener, Toolbar.OnMenuItemClickListener {
    private static final int RC_TAKE_ORDER = 1;
    private static final int RC_SIGN_IN = 100;
    ActivityOrderDetailsBinding mBinding;
    private List<String> mNamesList;
    private List<User> mUserList;
    private OrderDetailsAdapter mOrderDetailsAdapter;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDatabaseReferenceItems;
    private DatabaseReference mDatabaseReferenceUsers;
    private DatabaseReference mDatabaseReferenceNames;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private List<OrderPerUser> mOrderPerUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mBinding.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.tvLabel.setText(getString(R.string.number_of_orders_, 0));

        mBinding.toolbar.setOnMenuItemClickListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Initialize Firebase Authentation
        mFirebaseAuth = FirebaseAuth.getInstance();

        mNamesList = new ArrayList<>();
        mUserList = new ArrayList<>();

        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mBinding.autoCompleteTvUserName.getText().toString().trim();
                if (!TextUtils.isEmpty(userName)) {
                    if (!ifNamesAvailableInUsersListGetID(userName).first) {
                        startActivityForResult(TakeOrderActivity.newIntent(OrderDetailsActivity.this, mBinding.autoCompleteTvUserName.getText().toString(), null), RC_TAKE_ORDER);
                    } else {
                        Toast.makeText(OrderDetailsActivity.this, "Order with same name already available,Please edit.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Enter user name to proceed further", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mOrderDetailsAdapter = new OrderDetailsAdapter(this);
        mBinding.recyclerView.setAdapter(mOrderDetailsAdapter);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // already signed in
                    onSignedInInitialize(user.getUid());
                } else {
                    onSignedOutCleanup();
                    // not signed in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed In Cancelled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private ValueEventListener mValueEventListenerItems;
    private ValueEventListener mValueEventListenerUsers;
    private ValueEventListener mValueEventListenerNames;

    private void onSignedInInitialize(String uid) {
        DatabaseReference databaseReferenceUser = mFirebaseDatabase.getReference(uid);
        mDatabaseReferenceItems = databaseReferenceUser.child("items");
        mDatabaseReferenceUsers = databaseReferenceUser.child("users");
        mDatabaseReferenceNames = databaseReferenceUser.child("names");
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mOrderDetailsAdapter.setItemInfoArrayList(new ArrayList<>());
        detachDatabaseReadListener();
/*
        mBinding.progressCircular.setVisibility(View.VISIBLE);
*/
    }

    private void attachDatabaseReadListener() {
        if (mValueEventListenerUsers == null) {
            mValueEventListenerUsers = new ValueEventListener() {
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
                        mBinding.tvLabel.setText(getString(R.string.number_of_orders_, mOrderPerUserList.size()));
                        mBinding.progressCircular.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(OrderDetailsActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    mBinding.progressCircular.setVisibility(View.GONE);
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
                            com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item item
                                    = new com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item(id, itemInfo.getItemName(), String.valueOf(itemInfo.getItemPrice()), itemInfo.getMaxItemAllowed());
                            mDatabaseReferenceItems.child(id).setValue(item);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mDatabaseReferenceItems.addValueEventListener(mValueEventListenerItems);
        }
        if (mValueEventListenerNames == null) {
            mValueEventListenerNames = new ValueEventListener() {
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
            };
            mDatabaseReferenceNames.addValueEventListener(mValueEventListenerNames);
        }
    }

    private void detachDatabaseReadListener() {
        if (mValueEventListenerUsers != null) {
            mDatabaseReferenceUsers.removeEventListener(mValueEventListenerUsers);
            mValueEventListenerUsers = null;
        }
        if (mValueEventListenerItems != null) {
            mDatabaseReferenceItems.removeEventListener(mValueEventListenerItems);
            mValueEventListenerItems = null;
        }
        if (mValueEventListenerNames != null) {
            mDatabaseReferenceNames.removeEventListener(mValueEventListenerNames);
            mValueEventListenerNames = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        startActivity(TakeOrderActivity.newIntent(this, null, orderPerUser));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mOrderDetailsAdapter.setItemInfoArrayList(new ArrayList<>());
        mNamesList.clear();
        mUserList.clear();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return false;
        }
    }
}
