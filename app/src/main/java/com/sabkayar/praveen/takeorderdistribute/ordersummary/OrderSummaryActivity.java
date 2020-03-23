package com.sabkayar.praveen.takeorderdistribute.ordersummary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sabkayar.praveen.takeorderdistribute.R;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.OrderDetailsActivity;
import com.sabkayar.praveen.takeorderdistribute.orderDetails.model.OrderPerUser;
import com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.ordersummary.OrderSummaryFragment;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.TakeOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    public static Intent newIntent(OrderDetailsActivity orderDetailsActivity) {
        return new Intent(orderDetailsActivity,OrderSummaryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_activity);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Order Summary");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrderSummaryFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_summary).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    }
