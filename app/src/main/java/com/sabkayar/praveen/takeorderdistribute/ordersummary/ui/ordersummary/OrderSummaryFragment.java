package com.sabkayar.praveen.takeorderdistribute.ordersummary.ui.ordersummary;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sabkayar.praveen.takeorderdistribute.R;


public class OrderSummaryFragment extends Fragment {

    private OrderSummaryViewModel mViewModel;

    public static OrderSummaryFragment newInstance() {
        return new OrderSummaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_summary_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderSummaryViewModel.class);
        // TODO: Use the ViewModel
    }

}
