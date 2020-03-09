package com.sabkayar.praveen.takeorderdistribute.takeOrder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityTakeOrderBinding;
import com.sabkayar.praveen.takeorderdistribute.databinding.DialogAddItemBinding;
import com.sabkayar.praveen.takeorderdistribute.takeOrder.adapter.ItemInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TakeOrderActivity extends AppCompatActivity implements ItemInfoAdapter.OnEditClickListener, View.OnClickListener {

    private ActivityTakeOrderBinding mBinding;
    private ItemInfoAdapter mItemInfoAdapter;

    public static Intent newIntent(Context context, Object o) {
        Intent intent = new Intent(context, TakeOrderActivity.class);
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
                alertDialog.cancel();
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(TakeOrderActivity.this).takeOrderDao().updateItem(itemInfo.getItemId(), Objects.requireNonNull(dialogAddItemBinding.etItemName.getText()).toString(),Integer.valueOf(dialogAddItemBinding.etItemPrice.getText().toString()),Integer.valueOf(dialogAddItemBinding.etMaxItemsAllowed.getText().toString()));
                    }
                });
            }
        });
        alertDialog.show();
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
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
        }
    }
}
