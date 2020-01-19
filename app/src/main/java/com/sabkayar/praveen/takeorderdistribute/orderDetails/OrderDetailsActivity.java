package com.sabkayar.praveen.takeorderdistribute.orderDetails;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityOrderDetailsBinding;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.TakeOrderActivity;

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
    }
}
