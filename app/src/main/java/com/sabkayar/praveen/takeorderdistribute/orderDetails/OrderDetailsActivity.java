package com.sabkayar.praveen.takeorderdistribute.orderDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityOrderDetailsBinding;

import com.sabkayar.praveen.takeorderdistribute.orderDetails.adapter.OrderDetailsAdapter;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.TakeOrderActivity;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.Utils;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter.ItemInfoAdapter;


import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDetailsAdapter.OnListItemListener {
    private static final int RC_TAKE_ORDER = 1;
    ActivityOrderDetailsBinding mBinding;
    private List<OrderDetail> mOrderDetails;
    private OrderDetailsAdapter mOrderDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mBinding.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.tvLabel.setText(getString(R.string.number_of_orders_, 0));


        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TakeOrderActivity.newIntent(OrderDetailsActivity.this,mBinding.autoCompleteTvUserName.getText().toString() ), RC_TAKE_ORDER);
            }
        });

        AppDatabase.getInstance(this).takeOrderDao().getItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if(items.size()==0){
                    Toast.makeText(OrderDetailsActivity.this,"Empty list",Toast.LENGTH_SHORT).show();
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            for(Item itemInfo: Utils.getDummyList()){
                                Item item=new Item(itemInfo.getItemName(),itemInfo.getItemPrice(),itemInfo.getMaxItemAllowed(),itemInfo.getIsChecked());
                                AppDatabase.getInstance(OrderDetailsActivity.this).takeOrderDao()
                                        .insert(item);
                            }
                        }
                    });
                }else {
                    Toast.makeText(OrderDetailsActivity.this,"Sit silently",Toast.LENGTH_SHORT).show();

                }
            }
        });

        AppDatabase.getInstance(this).takeOrderDao().getAllUsers().observe(this, new Observer<List<UserName>>() {
            @Override
            public void onChanged(List<UserName> userNames) {
                List<String> userNamesList=new ArrayList<>();
                for(UserName userName:userNames){
                    userNamesList.add(userName.getUserName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderDetailsActivity.this,
                        android.R.layout.simple_dropdown_item_1line, userNamesList);
                mBinding.autoCompleteTvUserName.setAdapter(adapter);
                mBinding.autoCompleteTvUserName.setThreshold(1);
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mOrderDetailsAdapter = new OrderDetailsAdapter(this);
        mBinding.recyclerView.setAdapter(mOrderDetailsAdapter);

        AppDatabase.getInstance(this).takeOrderDao().getOrderDetails().observe(this, new Observer<List<OrderDetail>>() {
            @Override
            public void onChanged(List<OrderDetail> orderDetails) {
              mOrderDetailsAdapter.setItemInfoArrayList((ArrayList<OrderDetail>) orderDetails);
            }
        });


    }

    @Override
    public void onItemClick(OrderDetail orderDetail) {

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_TAKE_ORDER){
           if(resultCode==RESULT_OK){

               }
           }
        }
    }*/




}
