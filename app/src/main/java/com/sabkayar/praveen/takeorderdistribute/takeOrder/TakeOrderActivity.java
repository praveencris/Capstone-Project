package com.sabkayar.praveen.takeorderdistribute.takeOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.AppDatabase;
import com.sabkayar.praveen.takeorderdistribute.database.AppExecutors;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.database.entity.OrderDetail;
import com.sabkayar.praveen.takeorderdistribute.database.entity.UserName;
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityTakeOrderBinding;
import com.sabkayar.praveen.takeorderdistribute.databinding.DialogAddItemBinding;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter.ItemInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class TakeOrderActivity extends AppCompatActivity implements ItemInfoAdapter.OnListItemListener, View.OnClickListener {

    public static final String EXTRA_EDITED_ITEMS_LIST_DETAILS = "extra_order_details" + TakeOrderActivity.class.getSimpleName();
    public static final String EXTRA_ORDER_DETAILS = "extra_order_details" + TakeOrderActivity.class.getSimpleName();
    private static final String EXTRA_USER_NAME = "extra_user_name" + TakeOrderActivity.class.getSimpleName();
    private ActivityTakeOrderBinding mBinding;
    private ItemInfoAdapter mItemInfoAdapter;
    private List<Item> mSelectedItemsList, mEditedItemsList;
    private String mUserName;
    private List<OrderDetail> mOrderDetails;

    public static Intent newIntent(Context context, String userName) {
        Intent intent = new Intent(context, TakeOrderActivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
/*
        intent.putParcelableArrayListExtra(EXTRA_EDITED_ITEMS_LIST_DETAILS, (ArrayList<? extends Parcelable>) items);
*/
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_order);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Take Order");
        }
/*
        mEditedItemsList=getIntent().getParcelableArrayListExtra(EXTRA_EDITED_ITEMS_LIST_DETAILS);
*/
        mUserName = getIntent().getStringExtra(EXTRA_USER_NAME);
        mBinding.tvName.setText(mUserName);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(TakeOrderActivity.this).takeOrderDao()
                        .insert(new UserName(mUserName));
            }
        });

        mSelectedItemsList = new ArrayList<>();
        mOrderDetails = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mItemInfoAdapter = new ItemInfoAdapter(this);
        mBinding.recyclerView.setAdapter(mItemInfoAdapter);

        AppDatabase.getInstance(this).takeOrderDao().getItems().observe(this,
                new Observer<List<Item>>() {
                    @Override
                    public void onChanged(List<Item> items) {
                        mItemInfoAdapter.setItemInfoArrayList((ArrayList<Item>) items);
                    }
                });

        mBinding.floatingActionButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                       StringBuilder sb=new StringBuilder();
                        for (OrderDetail orderDetail : mOrderDetails) {
                          sb.append(orderDetail.getItemDesc()+",");
                        }
                        OrderDetail orderDetail=new OrderDetail(mUserName,sb.toString());
                        AppDatabase.getInstance(TakeOrderActivity.this)
                                .takeOrderDao().insert(orderDetail);
                    }
                });

               /* Intent intent=new Intent();
                intent.putParcelableArrayListExtra(EXTRA_ORDER_DETAILS, (ArrayList<? extends Parcelable>) mOrderDetails);
                setResult(Activity.RESULT_OK,intent);*/
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
                Item itemInfoLocal = new Item("", 0, 0, 0);
                itemInfoLocal.setItemId(itemInfo.getItemId());
                itemInfoLocal.setItemName(dialogAddItemBinding.etItemName.getText().toString());
                itemInfoLocal.setItemPrice(Integer.valueOf(dialogAddItemBinding.etItemPrice.getText().toString()));
                itemInfoLocal.setMaxItemAllowed((Integer.valueOf(dialogAddItemBinding.etMaxItemsAllowed.getText().toString())));
                itemInfoLocal.setIsChecked(0);

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(TakeOrderActivity.this).takeOrderDao().updateItem(itemInfoLocal);
                    }
                });
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onCheckBoxChecked(OrderDetail orderDetail, boolean isAdd) {
        orderDetail.setUserName(mUserName);
        if (isAdd) {
            mOrderDetails.add(orderDetail);
        } else {
            mOrderDetails.remove(orderDetail);
        }
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
                        Item item = new Item(dialogAddItemBinding.etItemName.getText().toString(),
                                Integer.valueOf(dialogAddItemBinding.etItemPrice.getText().toString()),
                                Integer.valueOf(dialogAddItemBinding.etMaxItemsAllowed.getText().toString()), 0);
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(TakeOrderActivity.this).takeOrderDao().insert(item);
                            }
                        });
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
        }
    }


}
