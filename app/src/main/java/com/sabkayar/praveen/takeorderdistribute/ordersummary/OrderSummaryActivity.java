package com.sabkayar.praveen.takeorderdistribute.ordersummary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.ordersummary.OrderSummaryFragment;

public class OrderSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrderSummaryFragment.newInstance())
                    .commitNow();
        }
    }
}
