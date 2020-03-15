package com.sabkayar.praveen.takeorderdistribute.takeOrder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.AppDatabase;
import com.sabkayar.praveen.takeorderdistribute.database.AppExecutors;
import com.sabkayar.praveen.takeorderdistribute.database.entity.UserName;
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityTakeOrderBinding;
import com.sabkayar.praveen.takeorderdistribute.databinding.DialogAddItemBinding;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.Item;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.realtimedbmodels.User;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter.ItemInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class TakeOrderActivity extends AppCompatActivity implements ItemInfoAdapter.OnListItemListener, View.OnClickListener {

    public static final String EXTRA_EDITED_ITEMS_LIST_DETAILS = "extra_order_details" + TakeOrderActivity.class.getSimpleName();
    public static final String EXTRA_ORDER_DETAILS = "extra_order_details" + TakeOrderActivity.class.getSimpleName();
    private static final String EXTRA_USER_NAME = "extra_user_name" + TakeOrderActivity.class.getSimpleName();
    private ActivityTakeOrderBinding mBinding;
    private ItemInfoAdapter mItemInfoAdapter;
    private String mUserName;
    private List<OrderDetail> mOrderDetails;

    DatabaseReference mDatabaseReferenceItems;
    DatabaseReference mDatabaseReferenceUsers;

    ValueEventListener mValueEventListener;

    public static Intent newIntent(Context context, String userName) {
        Intent intent = new Intent(context, TakeOrderActivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_order);

        mDatabaseReferenceItems = FirebaseDatabase.getInstance().getReference("items");
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Take Order");
        }
        mUserName = getIntent().getStringExtra(EXTRA_USER_NAME);
        mBinding.tvName.setText(mUserName);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(TakeOrderActivity.this).takeOrderDao()
                        .insert(new UserName(mUserName));
            }
        });
        mOrderDetails = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mItemInfoAdapter = new ItemInfoAdapter(this);
        mBinding.recyclerView.setAdapter(mItemInfoAdapter);
        mBinding.floatingActionButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mOrderDetails.isEmpty()) {
                    String id = mDatabaseReferenceUsers.push().getKey();
                    mDatabaseReferenceUsers.child(id).setValue(new User(id, mUserName));
                    for (OrderDetail orderDetail : mOrderDetails) {
                        mDatabaseReferenceUsers.child(id).child(orderDetail.getItemId()).setValue(orderDetail);
                    }
                }
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(Item itemInfo) {
        DialogAddItemBinding dialogAddItemBinding;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogAddItemBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_item, (ViewGroup) findViewById(android.R.id.content), false);
        builder.setView(dialogAddItemBinding.getRoot());

        dialogAddItemBinding.tvTitle.setText(R.string.edit_item);
        dialogAddItemBinding.etItemName.setText(itemInfo.getItemName());
        dialogAddItemBinding.etItemPrice.setText(String.valueOf(itemInfo.getItemPrice()));
        dialogAddItemBinding.etMaxItemsAllowed.setText(String.valueOf(itemInfo.getMaxItemAllowed()));

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddItemBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        dialogAddItemBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = dialogAddItemBinding.etItemName.getText().toString().trim();
                String itemPrice = dialogAddItemBinding.etItemPrice.getText().toString().trim();
                String maxItemAllowed = dialogAddItemBinding.etMaxItemsAllowed.getText().toString().trim();

                if (!TextUtils.isEmpty(itemName)
                        && !TextUtils.isEmpty(itemPrice)
                        && !TextUtils.isEmpty(maxItemAllowed)) {
                    if (Integer.valueOf(maxItemAllowed) >= 0) {
                        updateItem(new Item(itemInfo.getItemId(), itemName, itemPrice, Integer.valueOf(maxItemAllowed)));
                        alertDialog.cancel();
                    } else {
                        Toast.makeText(dialogAddItemBinding.btnDone.getContext(), "Enter valid items allowed to proceed further", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(dialogAddItemBinding.btnDone.getContext(), "Enter Valid details to proceed further", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.show();
    }

    private void updateItem(Item item) {
        final boolean[] isAllowedForUpdate = {false};
        mValueEventListener = null;
        mValueEventListener = mDatabaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                    if (userDataSnapshot.hasChildren()) {
                        for (DataSnapshot itemDataSnapshot : userDataSnapshot.getChildren()) {
                            if (itemDataSnapshot.hasChildren()) {
                                OrderDetail newOrderDetail = itemDataSnapshot.getValue(OrderDetail.class);
                                if (item.getItemId().equals(newOrderDetail.getItemId())) {
                                    newOrderDetail.setItemName(item.getItemName());
                                    if(item.getMaxItemAllowed()>=newOrderDetail.getItemCount()){
                                        itemDataSnapshot.getRef().setValue(newOrderDetail);
                                        isAllowedForUpdate[0]=true;
                                    }else {
                                        isAllowedForUpdate[0]=false;
                                        Toast.makeText(TakeOrderActivity.this, "Max count allowed can not be less then item count. Update item failed!", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                detachValueEventListener();
                if(isAllowedForUpdate[0]){
                    mDatabaseReferenceItems.child(item.getItemId()).setValue(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                detachValueEventListener();
            }
        });

    }

    private void detachValueEventListener() {
        if (mValueEventListener != null) {
            mDatabaseReferenceUsers.removeEventListener(mValueEventListener);
        }
    }

    private void addItem(Item item) {
        mDatabaseReferenceItems.child(item.getItemId()).setValue(item);
    }

    @Override
    public void onCheckBoxChecked(OrderDetail orderDetail, boolean isAdd) {
        if (isAdd) {
            mOrderDetails.add(orderDetail);
        } else {
            mOrderDetails.remove(orderDetail);
        }
    }

    @Override
    public void onItemLongPress(Item item) {
        showDeleteDialog(item);
    }

    private void showDeleteDialog(Item item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Do you want to delete item " + item.getItemName());
        dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                final boolean[] isItemAvailableInOrders = {false};
                mValueEventListener = null;
                mValueEventListener = mDatabaseReferenceUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                            if (userDataSnapshot.hasChildren()) {
                                for (DataSnapshot itemDataSnapshot : userDataSnapshot.getChildren()) {
                                    if (itemDataSnapshot.hasChildren()) {
                                        Item newItem = itemDataSnapshot.getValue(Item.class);
                                        if (item.getItemId().equals(newItem.getItemId())) {
                                            itemDataSnapshot.getRef().setValue(item);
                                            Toast.makeText(TakeOrderActivity.this, "Item " + item.getItemName() + " already present in one of the order. Deletion not allowed!", Toast.LENGTH_LONG).show();
                                            isItemAvailableInOrders[0] = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!isItemAvailableInOrders[0]) {
                            deleteItem(item);
                        }
                        detachValueEventListener();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        detachValueEventListener();
                    }
                });
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void deleteItem(Item item) {
        mDatabaseReferenceItems.child(item.getItemId()).removeValue();
        Toast.makeText(TakeOrderActivity.this, "Item " + item.getItemName() + " deleted successfully!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                DialogAddItemBinding dialogAddItemBinding;
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                dialogAddItemBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_item, (ViewGroup) findViewById(android.R.id.content), false);
                builder.setView(dialogAddItemBinding.getRoot());

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAddItemBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                dialogAddItemBinding.btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemName = dialogAddItemBinding.etItemName.getText().toString().trim();
                        String itemPrice = dialogAddItemBinding.etItemPrice.getText().toString().trim();
                        String maxItemAllowed = dialogAddItemBinding.etMaxItemsAllowed.getText().toString().trim();

                        if (!TextUtils.isEmpty(itemName)
                                && !TextUtils.isEmpty(itemPrice)
                                && !TextUtils.isEmpty(maxItemAllowed)) {
                            if (Integer.valueOf(maxItemAllowed) >= 0) {
                                String id = mDatabaseReferenceItems.push().getKey();
                                addItem(new Item(id, itemName, itemPrice, Integer.valueOf(maxItemAllowed)));
                                alertDialog.cancel();
                            } else {
                                Toast.makeText(dialogAddItemBinding.btnDone.getContext(), "Enter valid items allowed to proceed further", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(dialogAddItemBinding.btnDone.getContext(), "Enter Valid details to proceed further", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReferenceItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    itemList.add(item);
                }
                mItemInfoAdapter.setItemInfoArrayList(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
