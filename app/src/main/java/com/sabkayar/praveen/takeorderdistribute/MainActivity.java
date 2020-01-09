package com.sabkayar.praveen.takeorderdistribute;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sabkayar.praveen.takeorderdistribute.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.tvLabel.setText(getString(R.string.number_of_orders_,0));


    }
}
