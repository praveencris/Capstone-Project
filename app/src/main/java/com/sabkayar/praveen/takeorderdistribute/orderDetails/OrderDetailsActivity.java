package com.sabkayar.praveen.takeorderdistribute.orderDetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.database.AppDatabase;
import com.sabkayar.praveen.takeorderdistribute.database.AppExecutors;
import com.sabkayar.praveen.takeorderdistribute.database.entity.Item;
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityOrderDetailsBinding;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.TakeOrderActivity;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.Utils;


import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final int RC_TAKE_ORDER = 1;
    ActivityOrderDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mBinding.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.tvLabel.setText(getString(R.string.number_of_orders_, 0));


        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TakeOrderActivity.newIntent(OrderDetailsActivity.this, null), RC_TAKE_ORDER);
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
    }
}
